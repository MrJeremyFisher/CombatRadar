package com.aleksey.combatradar.gui.screens;

import com.aleksey.combatradar.config.GroupType;
import com.aleksey.combatradar.config.RadarConfig;
import com.aleksey.combatradar.config.RadarEntityInfo;
import com.aleksey.combatradar.gui.components.SmallButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.joml.Matrix3x2fStack;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Aleksey Terzi
 */
public class EntityScreen extends Screen {
    private static final int MAX_ENTITIES_PER_COL = 8;
    private static final int ICON_WIDTH = 12;
    private static final int LINE_HEIGHT = 16;
    private static final int BUTTON_WIDTH = 24;
    private static GroupType _activeGroupType = GroupType.NEUTRAL;
    private final RadarConfig _config;
    private final Screen _parent;
    private Button _enableButton;
    private Map<GroupType, EntityGroup> _groups;
    private int _titleTop;
    private int _buttonTop;
    private int _iconTop, _iconLeft;
    private EntityGroup _activeGroup;
    private String _groupName;
    private Button _neutralButton;
    private Button _aggressiveButton;
    private Button _otherButton;
    private final ArrayList<Button> _iconButtons;
    public EntityScreen(Screen parent, RadarConfig config) {
        super(Component.literal("Radar Entities"));
        _parent = parent;
        _config = config;
        _iconButtons = new ArrayList<>();
    }

    @Override
    protected void init() {
        _titleTop = this.height / 4 - 40;
        _buttonTop = this.height - this.height / 4 - 10;
        _iconTop = _titleTop + 16 + (this.height - (this.height - _buttonTop) - _titleTop - 16 - MAX_ENTITIES_PER_COL * LINE_HEIGHT) / 2;

        createEntityGroups();
        addFunctionalButtons();
        setActiveGroup(_activeGroupType);
    }

    private void addFunctionalButtons() {
        int y = _buttonTop;
        int x = this.width / 2 - 100;

        addRenderableWidget(_neutralButton = Button.builder(Component.literal("Neutral"), (btn) -> setActiveGroup(GroupType.NEUTRAL)).bounds(x, y, 66, 20).build());

        addRenderableWidget(_aggressiveButton = Button.builder(Component.literal("Aggressive"), (btn) -> setActiveGroup(GroupType.AGGRESSIVE)).bounds(x + 66 + 1, y, 66, 20).build());

        addRenderableWidget(_otherButton = Button.builder(Component.literal("Other"), (btn) -> setActiveGroup(GroupType.OTHER)).bounds(x + 66 + 1 + 66 + 1, y, 66, 20).build());

        y += 24;

        addRenderableWidget(_enableButton = Button.builder(getEnableButtonText(),
                (btn) -> {
                    _config.setGroupEnabled(_activeGroupType, !_config.isGroupEnabled(_activeGroupType));
                    _config.save();
                    _enableButton.setMessage(getEnableButtonText());
                }).bounds(x, y, 200, 20).build());

        y += 24;

        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (btn) -> this.minecraft.setScreen(_parent)).bounds(x, y, 200, 20).build());
    }

    private void addIconButtons() {
        int colIndex = 0;
        int rowIndex = 0;
        int colX = _iconLeft;
        int buttonX = colX + _activeGroup.getIconAndTextWidth(colIndex);
        int buttonY = _iconTop - 4;
        int buttonHeight = LINE_HEIGHT - 1;

        for (Button iconButton : _iconButtons)
            removeWidget(iconButton);

        _iconButtons.clear();

        for (RadarEntityInfo info : _activeGroup.entities) {
            if (rowIndex == MAX_ENTITIES_PER_COL) {
                colX += _activeGroup.getColWidth(colIndex);

                colIndex++;
                rowIndex = 0;

                buttonX = colX + _activeGroup.getIconAndTextWidth(colIndex);
                buttonY = _iconTop - 4;
            }

            String buttonText = info.getEnabled() ? "on" : "off";
            final String name = info.getName();
            Button iconButton = new SmallButton(buttonX, buttonY, BUTTON_WIDTH, buttonHeight, Component.literal(buttonText),
                    btn -> iconButtonClicked(btn, name));

            addRenderableWidget(iconButton);
            _iconButtons.add(iconButton);

            buttonY += LINE_HEIGHT;

            rowIndex++;
        }
    }

    private void iconButtonClicked(Button btn, String name) {
        RadarEntityInfo info = _config.getEntity(name);
        info.setEnabled(!info.getEnabled());
        _config.save();

        String text = info.getEnabled() ? "on" : "off";

        btn.setMessage(Component.literal(text));
    }

    private void createEntityGroups() {
        _groups = new HashMap<>();

        for (RadarEntityInfo info : _config.getEntityList()) {
            EntityGroup group = _groups.get(info.getGroupType());

            if (group == null)
                _groups.put(info.getGroupType(), group = new EntityGroup(info.getGroupType()));

            int colIndex = group.entities.size() / MAX_ENTITIES_PER_COL;
            int textWidth = this.font.width(info.getName());

            if (group.listColTextWidth.size() <= colIndex)
                group.listColTextWidth.add(textWidth);
            else if (group.listColTextWidth.get(colIndex) < textWidth)
                group.listColTextWidth.set(colIndex, textWidth);

            group.entities.add(info);
        }
    }

    private void setActiveGroup(GroupType groupType) {
        _activeGroupType = groupType;
        _activeGroup = _groups.get(groupType);
        _iconLeft = (this.width - _activeGroup.getTotalWidth() + 25) / 2;

        switch (groupType) {
            case NEUTRAL -> _groupName = "Neutral";
            case AGGRESSIVE -> _groupName = "Aggressive";
            case OTHER -> _groupName = "Other";
        }

        addIconButtons();

        _neutralButton.active = groupType != GroupType.NEUTRAL;
        _aggressiveButton.active = groupType != GroupType.AGGRESSIVE;
        _otherButton.active = groupType != GroupType.OTHER;

        _enableButton.setMessage(getEnableButtonText());
    }

    private Component getEnableButtonText() {
        String text = _groupName + " Entities: " + (_config.isGroupEnabled(_activeGroupType) ? "On" : "Off");
        return Component.literal(text);

    }

    @Override
    public void removed() {
        _config.save();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, _titleTop, Color.WHITE.getRGB());
        renderIcons(guiGraphics);
    }

    private void renderIcons(GuiGraphics guiGraphics) {
        int colIndex = 0;
        int rowIndex = 0;
        int x = _iconLeft;
        int y = _iconTop;

        for (RadarEntityInfo info : _activeGroup.entities) {
            if (rowIndex == MAX_ENTITIES_PER_COL) {
                x += _activeGroup.getColWidth(colIndex);
                y = _iconTop;

                colIndex++;
                rowIndex = 0;
            }

            renderIcon(guiGraphics, x, y + 4, info);

            boolean isEnabled = info.getEnabled() && _config.isGroupEnabled(_activeGroupType);
            Color color = isEnabled ? Color.WHITE : Color.DARK_GRAY;
            guiGraphics.drawString(this.font, info.getName(), x + ICON_WIDTH, y, color.getRGB(), true);

            y += LINE_HEIGHT;

            rowIndex++;
        }
    }

    private void renderIcon(GuiGraphics guiGraphics, float x, float y, RadarEntityInfo info) {
        Matrix3x2fStack poseStack = guiGraphics.pose();

        poseStack.pushMatrix();
        poseStack.translate(x, y);
        poseStack.scale(0.6f, 0.6f);

//        RenderSystem.setShaderTexture(0, Minecraft.getInstance().getTextureManager().getTexture(info.getIcon((Entity) null)).getTextureView());

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, info.getIcon(info.getEntityClassName()), -8, -8, 0, 0, 16, 16, 16, 16);

        poseStack.popMatrix();
    }

    private static class EntityGroup {
        public GroupType groupType;
        public List<RadarEntityInfo> entities;
        public List<Integer> listColTextWidth;

        public EntityGroup(GroupType groupType) {
            this.groupType = groupType;
            this.entities = new ArrayList<>();
            this.listColTextWidth = new ArrayList<>();
        }

        public int getColWidth(int colIndex) {
            return ICON_WIDTH + listColTextWidth.get(colIndex) + BUTTON_WIDTH + 25;
        }

        public int getIconAndTextWidth(int colIndex) {
            return ICON_WIDTH + listColTextWidth.get(colIndex) + 1;
        }

        public int getTotalWidth() {
            int totalWidth = 0;

            for (int i = 0; i < listColTextWidth.size(); i++)
                totalWidth += getColWidth(i);

            return totalWidth;
        }
    }
}