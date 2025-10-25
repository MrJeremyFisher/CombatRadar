package com.aleksey.combatradar.gui.screens;

import com.aleksey.combatradar.config.PlayerType;
import com.aleksey.combatradar.config.RadarConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.awt.Color;

/**
 * @author Aleksey Terzi
 */
public class ManagePlayersScreen extends Screen {
    private static final int SLOT_HEIGHT = 16;
    private static PlayerType _activePlayerType = PlayerType.Ally;
    private final RadarConfig _config;
    private final Screen _parent;
    private Button _allyButton;
    private Button _enemyButton;
    private Button _deleteButton;
    private PlayerListContainer _playerListContainer;

    public ManagePlayersScreen(Screen parent, RadarConfig config) {
        super(Component.literal("Manage Players"));
        _parent = parent;
        _config = config;
    }

    @Override
    public void init() {
        int x = this.width / 2 - 100;
        int y = this.height - 72;

        _playerListContainer = addRenderableWidget(new PlayerListContainer());

        _allyButton = addRenderableWidget(Button.builder(Component.literal("Allies"), (btn) -> loadPlayers(PlayerType.Ally)).bounds(x, y, 100, 20).build());

        _enemyButton = addRenderableWidget(Button.builder(Component.literal("Enemies"), (btn) -> loadPlayers(PlayerType.Enemy)).bounds(x + 101, y, 100, 20).build());

        y += 24;

        addRenderableWidget(Button.builder(Component.literal("Add Player"), (btn) -> {
            this.minecraft.setScreen(new AddPlayerScreen(this, _config, _activePlayerType));
        }).bounds(x, y, 100, 20).build());

        _deleteButton = addRenderableWidget(Button.builder(Component.literal("Delete Player"), (btn) -> deletePlayer()).bounds(x + 101, y, 100, 20).build());

        y += 24;

        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (btn) -> this.minecraft.setScreen(_parent)).bounds(x, y, 200, 20).build());

        loadPlayers(_activePlayerType);
    }

    private void loadPlayers(PlayerType playerType) {
        _activePlayerType = playerType;

        _playerListContainer.replaceEntries(_config.getPlayers(playerType).stream().map(PlayerListItem::new).toList());

        _allyButton.active = playerType != PlayerType.Ally;
        _enemyButton.active = playerType != PlayerType.Enemy;
        _deleteButton.active = false;
    }

    private void deletePlayer() {
        String playerName = _playerListContainer.getSelected().getPlayerName();

        _config.setPlayerType(playerName, PlayerType.Neutral);
        _config.save();

        loadPlayers(_activePlayerType);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, Color.WHITE.getRGB());
    }

    private class PlayerListItem extends ObjectSelectionList.Entry<PlayerListItem> {
        private final String _playerName;

        public PlayerListItem(String playerName) {
            _playerName = playerName;
        }

        public String getPlayerName() {
            return _playerName;
        }

        @Override
        public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean isHovering, float partialTick) {
            guiGraphics.drawString(ManagePlayersScreen.this.font, _playerName, this.getContentX() + 1, this.getContentY() + 1, Color.WHITE.getRGB(), true);
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
            ManagePlayersScreen.this._playerListContainer.setSelected(this);
            ManagePlayersScreen.this._deleteButton.active = true;

            return true;
        }

        @Override
        public Component getNarration() {
            return null;
        }
    }

    private class PlayerListContainer extends AbstractSelectionList<PlayerListItem> {
        public PlayerListContainer() {
            super(ManagePlayersScreen.this.minecraft, ManagePlayersScreen.this.width, ManagePlayersScreen.this.height - 108, 32, SLOT_HEIGHT);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        }
    }
}