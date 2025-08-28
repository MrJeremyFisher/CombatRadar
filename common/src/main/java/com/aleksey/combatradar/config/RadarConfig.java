package com.aleksey.combatradar.config;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Bogged;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Aleksey Terzi
 */
public class RadarConfig {
    private final File _configFile;
    private final KeyMapping _settingsKey;
    private final List<RadarEntityInfo> _entityList;
    private final Map<String, RadarEntityInfo> _entityMap;
    private final Map<GroupType, Boolean> _groups;
    private final Map<String, PlayerInfo> _players;
    private final Map<PlayerType, PlayerTypeInfo> _playerTypes;
    private boolean _enabled = true;
    private boolean _pingsEnabled = true;
    private boolean _speedometerEnabled = false;
    private float _radarOpacity = 0.5f;
    private Color _radarColor = new Color(128, 128, 128);
    private float _radarSize = 0.4f;
    private int _radarDistance = 128;
    private float _radarX = 0;
    private float _radarY = 0;
    private float _iconScale = 0.8869566f;
    private float _fontScale = 1.4f;
    private boolean _showPlayerNames = true;
    private boolean _showExtraPlayerInfo = true;
    private boolean _logPlayerStatus = true;
    private boolean _showYLevel = false;
    private boolean _useLogScale = false;
    private List<String> _playersExcludedFromLog;
    // Calculated settings
    private boolean _isJourneyMapEnabled;
    private boolean _isVoxelMapEnabled;
    private boolean _isXaerosEnabled;
    public RadarConfig(File file, KeyMapping settingsKey) {
        _configFile = file;
        _settingsKey = settingsKey;

        // Entity icons from here https://minecraft.wiki/w/Category:Mob_faces
        _entityList = new ArrayList<>();
        _entityList.add(new RadarEntityInfo(Bat.class, "Bat", "icons/bat.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Chicken.class, "Chicken", "icons/chicken.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Cow.class, "Cow", "icons/cow/cow.png", GroupType.NEUTRAL));
        _entityList.add(new RadarHorseInfo("Horse", "icons/horse/horse_chestnut.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Mule.class, "Mule", "icons/horse/mule.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Donkey.class, "Donkey", "icons/horse/donkey.png", GroupType.NEUTRAL));
        _entityList.add(
                new RadarEntityInfo(Llama.class, "Llama", "icons/llama/llama.png", GroupType.NEUTRAL)
                        .addEntity(TraderLlama.class, "icons/llama/llama_trader.png")
        );
        _entityList.add(new RadarEntityInfo(MushroomCow.class, "Mooshroom", "icons/cow/mooshroom.png", GroupType.NEUTRAL));
        _entityList.add(
                new RadarEntityInfo(Ocelot.class, "Ocelot", "icons/cat/ocelot.png", GroupType.NEUTRAL)
                        .addEntity(Cat.class, "icons/cat/black.png")
        );
        _entityList.add(new RadarEntityInfo(Pig.class, "Pig", "icons/pig/pig.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Rabbit.class, "Rabbit", "icons/rabbit/white.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Sheep.class, "Sheep", "icons/sheep/sheep.png", GroupType.NEUTRAL));
        _entityList.add(
                new RadarEntityInfo(Squid.class, "Squid", "icons/squid.png", GroupType.NEUTRAL)
                        .addEntity(GlowSquid.class, "icons/squid_glow.png")
        );
        _entityList.add(new RadarEntityInfo(Villager.class, "Villager", "icons/villager/villager.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Wolf.class, "Wolf", "icons/wolf/wolf.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Blaze.class, "Blaze", "icons/blaze.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(CaveSpider.class, "Cave Spider", "icons/spider/cave_spider.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(Creeper.class, "Creeper", "icons/creeper.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(EnderMan.class, "Enderman", "icons/enderman/enderman.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(Ghast.class, "Ghast", "icons/ghast/ghast.png", GroupType.AGGRESSIVE));
        _entityList.add(
                new RadarEntityInfo(Guardian.class, "Guardian", "icons/guardian.png", GroupType.AGGRESSIVE)
                        .addEntity(ElderGuardian.class, "icons/elder_guardian.png")
        );
        _entityList.add(new RadarEntityInfo(IronGolem.class, "Iron Golem", "icons/iron_golem.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(MagmaCube.class, "Magma Cube", "icons/slime/magmacube.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(Silverfish.class, "Silverfish", "icons/silverfish.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(Skeleton.class, "Skeleton", "icons/skeleton/skeleton.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(Slime.class, "Slime", "icons/slime/slime.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(SnowGolem.class, "Snow Golem", "icons/snowman.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Spider.class, "Spider", "icons/spider/spider.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(Witch.class, "Witch", "icons/witch.png", GroupType.AGGRESSIVE));
        _entityList.add(
                new RadarEntityInfo(Zombie.class, "Zombie", "icons/zombie/zombie.png", GroupType.AGGRESSIVE)
                        .addEntity(Drowned.class, "icons/zombie/drowned.png")
                        .addEntity(Husk.class, "icons/zombie/husk.png")
        );
        _entityList.add(new RadarEntityInfo(ItemEntity.class, "Item", "icons/item.png", GroupType.OTHER));
        _entityList.add(new RadarEntityInfo(Boat.class, "Boat", "icons/boat.png", GroupType.OTHER));
        _entityList.add(new RadarEntityInfo(AbstractMinecart.class, "Minecart", "icons/minecart.png", GroupType.OTHER));
        _entityList.add(new PlayerRadarEntityInfo(PlayerType.Neutral, "Player (Neutral)", "icons/player.png", GroupType.OTHER));
        _entityList.add(new PlayerRadarEntityInfo(PlayerType.Ally, "Player (Ally)", "icons/player.png", GroupType.OTHER));
        _entityList.add(new PlayerRadarEntityInfo(PlayerType.Enemy, "Player (Enemy)", "icons/player.png", GroupType.OTHER));
        _entityList.add(new RadarEntityInfo(PolarBear.class, "Polar Bear", "icons/bear/polarbear.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Shulker.class, "Shulker", "icons/shulker/shulker.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Stray.class, "Stray", "icons/skeleton/stray.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(ExperienceOrb.class, "XP Orb", "icons/xp_orb.png", GroupType.OTHER));
        _entityList.add(new RadarEntityInfo(WitherBoss.class, "Wither", "icons/wither/wither.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(WitherSkeleton.class, "Wither Skeleton", "icons/skeleton/wither_skeleton.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(Parrot.class, "Parrot", "icons/parrot/parrot.png", GroupType.NEUTRAL));

        _entityList.add(
                new RadarEntityInfo(Evoker.class, "Illager", "icons/illager/evoker.png", GroupType.AGGRESSIVE)
                        .addEntity(Illusioner.class, "icons/illager/illusioner.png")
                        .addEntity(Vex.class, "icons/illager/vex.png")
                        .addEntity(Vindicator.class, "icons/illager/vindicator.png")
                        .addEntity(Pillager.class, "icons/illager/pillager.png")
                        .addEntity(Ravager.class, "icons/illager/ravager.png")
        );

        _entityList.add(new RadarEntityInfo(Axolotl.class, "Axolotl", "icons/axolotl.png", GroupType.NEUTRAL));
        _entityList.add(
                new RadarEntityInfo(Salmon.class, "Fish", "icons/salmon.png", GroupType.NEUTRAL)
                        .addEntity(Cod.class, "icons/cod.png")
                        .addEntity(Pufferfish.class, "icons/pufferfish.png")
                        .addEntity(TropicalFish.class, "icons/tropical_fish.png")
        );
        _entityList.add(new RadarEntityInfo(Fox.class, "Fox", "icons/fox.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Strider.class, "Strider", "icons/strider.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Turtle.class, "Turtle", "icons/turtle.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Turtle.class, "Trader", "icons/wandering_trader.png", GroupType.NEUTRAL));

        _entityList.add(new RadarEntityInfo(Bee.class, "Bee", "icons/bee.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Dolphin.class, "Dolphin", "icons/dolphin.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Goat.class, "Goat", "icons/goat.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Panda.class, "Panda", "icons/panda.png", GroupType.NEUTRAL));
        _entityList.add(
                new RadarEntityInfo(Piglin.class, "Piglin", "icons/piglin.png", GroupType.NEUTRAL)
                        .addEntity(ZombifiedPiglin.class, "icons/zombie_pigman.png")
        );

        _entityList.add(new RadarEntityInfo(Endermite.class, "Endermite", "icons/endermite.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(Endermite.class, "Hoglin", "icons/hoglin.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(Phantom.class, "Phantom", "icons/phantom.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(PiglinBrute.class, "Piglin Brute", "icons/piglin_brute.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(Zoglin.class, "Zoglin", "icons/zoglin.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(ZombieVillager.class, "Zombie Villager", "icons/zombie_villager.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(EnderDragon.class, "Ender Dragon", "icons/dragon.png", GroupType.AGGRESSIVE));

        _entityList.add(new RadarEntityInfo(Allay.class, "Allay", "icons/allay.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Frog.class, "Frog", "icons/frog.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Tadpole.class, "Tadpole", "icons/tadpole.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Warden.class, "Warden", "icons/warden.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(ChestBoat.class, "Boat With Chest", "icons/boat_chest.png", GroupType.OTHER));

        _entityList.add(new RadarEntityInfo(Camel.class, "Camel", "icons/camel.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Sniffer.class, "Sniffer", "icons/sniffer.png", GroupType.NEUTRAL));

        _entityList.add(new RadarEntityInfo(Armadillo.class, "Armadillo", "icons/armadillo.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Bogged.class, "Bogged", "icons/bogged.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(Breeze.class, "Breeze", "icons/breeze.png", GroupType.AGGRESSIVE));

        _entityList.add(new RadarEntityInfo(Creaking.class, "Creaking", "icons/creaking.png", GroupType.AGGRESSIVE));

        _entityList.sort(new RadarEntityInfo.EntityComparator());

        _entityMap = new HashMap<>();

        for (RadarEntityInfo radarEntityInfo : _entityList) radarEntityInfo.addToMap(_entityMap);

        _groups = new HashMap<>();
        _groups.put(GroupType.NEUTRAL, true);
        _groups.put(GroupType.AGGRESSIVE, true);
        _groups.put(GroupType.OTHER, true);

        _players = new HashMap<>();

        _playerTypes = new HashMap<>();
        _playerTypes.put(PlayerType.Neutral, new PlayerTypeInfo(Color.WHITE));
        _playerTypes.put(PlayerType.Ally, new PlayerTypeInfo(Color.GREEN));
        _playerTypes.put(PlayerType.Enemy, new PlayerTypeInfo(Color.YELLOW));

        _playersExcludedFromLog = new ArrayList<>();
        _playersExcludedFromLog.add("~BTLP SLOT");
    }

    public KeyMapping getSettingsKey() {
        return _settingsKey;
    }

    public boolean getEnabled() {
        return _enabled;
    }

    public void setEnabled(boolean value) {
        _enabled = value;
    }

    public boolean getPingsEnabled() {
        return _pingsEnabled;
    }

    public void setPingsEnabled(boolean value) {
        _pingsEnabled = value;
    }

    public boolean getSpeedometerEnabled() {
        return _speedometerEnabled;
    }

    public void setSpeedometerEnabled(boolean value) {
        _speedometerEnabled = value;
    }

    public float getRadarOpacity() {
        return _radarOpacity;
    }

    public boolean setRadarOpacity(float value) {
        if (_radarOpacity == value)
            return false;

        _radarOpacity = value;

        return true;
    }

    public Color getRadarColor() {
        return _radarColor;
    }

    public boolean setRadarColor(Color value) {
        if (Objects.equals(_radarColor, value))
            return false;

        _radarColor = value;

        return true;
    }

    public float getRadarSize() {
        return _radarSize;
    }

    public boolean setRadarSize(float value) {
        if (_radarSize == value)
            return false;

        _radarSize = value;

        return true;
    }

    public int getRadarDistance() {
        return _radarDistance;
    }

    public boolean setRadarDistance(int value) {
        if (_radarDistance == value)
            return false;

        _radarDistance = value;

        return true;
    }

    public float getRadarX() {
        return _radarX;
    }

    public void setRadarX(float value) {
        _radarX = value;
    }

    public float getRadarY() {
        return _radarY;
    }

    public void setRadarY(float value) {
        _radarY = value;
    }

    public float getIconScale() {
        return _iconScale;
    }

    public boolean setIconScale(float value) {
        if (_iconScale == value)
            return false;

        _iconScale = value;

        return true;
    }

    public float getFontScale() {
        return _fontScale;
    }

    public boolean setFontScale(float value) {
        if (_fontScale == value)
            return false;

        _fontScale = value;

        return true;
    }

    public PlayerTypeInfo getPlayerTypeInfo(PlayerType playerType) {
        return _playerTypes.get(playerType);
    }

    public boolean getShowPlayerNames() {
        return _showPlayerNames;
    }

    public void setShowPlayerNames(boolean value) {
        _showPlayerNames = value;
    }

    public boolean getShowExtraPlayerInfo() {
        return _showExtraPlayerInfo;
    }

    public void setShowExtraPlayerInfo(boolean value) {
        _showExtraPlayerInfo = value;
    }

    public boolean getLogPlayerStatus() {
        return _logPlayerStatus;
    }

    public void setLogPlayerStatus(boolean value) {
        _logPlayerStatus = value;
    }

    public boolean getShowYLevel() {
        return _showYLevel;
    }

    public void setShowYLevel(boolean value) {
        _showYLevel = value;
    }

    public List<RadarEntityInfo> getEntityList() {
        return _entityList;
    }

    public RadarEntityInfo getEntity(String name) {
        for (RadarEntityInfo info : _entityList) {
            if (info.getName().equalsIgnoreCase(name))
                return info;
        }

        return null;
    }

    public void setEntityEnabled(String name, boolean enabled) {
        getEntity(name).setEnabled(enabled);
    }

    public ResourceLocation getEnabledIcon(Entity entity) {
        String entityClass;

        if (entity instanceof ItemEntity) {
            entityClass = ItemEntity.class.getCanonicalName();
        } else if (entity instanceof Boat) {
            entityClass = Boat.class.getCanonicalName();
        } else if (entity instanceof AbstractMinecart) {
            entityClass = AbstractMinecart.class.getCanonicalName();
        } else if (entity instanceof Player) {
            var playerType = getPlayerType(entity.getName().getString());
            entityClass = Player.class.getCanonicalName() + "." + playerType;
        } else {
            entityClass = entity.getClass().getCanonicalName();
        }

        var info = _entityMap.getOrDefault(entityClass, null);

        return info != null && info.getEnabled() && _groups.get(info.getGroupType())
                ? info.getIcon(entity)
                : null;
    }

    public boolean isGroupEnabled(GroupType groupType) {
        return _groups.get(groupType);
    }

    public void setGroupEnabled(GroupType groupType, boolean value) {
        _groups.put(groupType, value);
    }

    public PlayerType getPlayerType(String playerName) {
        String key = playerName.toLowerCase();
        PlayerInfo info = _players.get(key);

        return info != null ? info.type : PlayerType.Neutral;
    }

    public void revertNeutralAggressive() {
        boolean enabled = _groups.get(GroupType.NEUTRAL) || _groups.get(GroupType.AGGRESSIVE);

        _groups.put(GroupType.NEUTRAL, !enabled);
        _groups.put(GroupType.AGGRESSIVE, !enabled);
    }

    public void setPlayerType(String playerName, PlayerType playerType) {
        String key = playerName.toLowerCase();

        if (playerType == PlayerType.Neutral) {
            _players.remove(key);
            return;
        }

        PlayerInfo info = _players.get(key);

        if (info == null)
            _players.put(key, new PlayerInfo(playerName, playerType));
        else
            info.type = playerType;
    }

    public List<String> getPlayers(PlayerType playerType) {
        List<String> result = new ArrayList<>();

        for (PlayerInfo info : _players.values()) {
            if (info.type == playerType)
                result.add(info.name);
        }

        Collections.sort(result);

        return result;
    }

    public boolean getIsJourneyMapEnabled() {
        return _isJourneyMapEnabled;
    }

    public void setIsJourneyMapEnabled(boolean value) {
        _isJourneyMapEnabled = value;
    }

    public boolean getIsVoxelMapEnabled() {
        return _isVoxelMapEnabled;
    }

    public void setIsVoxelMapEnabled(boolean value) {
        _isVoxelMapEnabled = value;
    }

    public boolean getIsXaerosEnabled() {
        return _isXaerosEnabled;
    }

    public void setIsXaerosEnabled(boolean value) {
        _isXaerosEnabled = value;
    }

    public List<String> getPlayersExcludedFromLog() {
        return _playersExcludedFromLog;
    }

    public void setPlayersExcludedFromLog(List<String> value) {
        _playersExcludedFromLog = value;

        _playersExcludedFromLog.replaceAll(String::toUpperCase);
    }

    public boolean isPlayerExcluded(String playerName) {
        String upperPlayerName = playerName.toUpperCase();

        for (String p : _playersExcludedFromLog) {
            if (upperPlayerName.startsWith(p)) {
                return true;
            }
        }

        return false;
    }

    public void save() {
        RadarConfigLoader.save(this, _configFile);
    }

    public boolean load() {
        return RadarConfigLoader.load(this, _configFile);
    }

    public void setLogScaleEnabled(boolean b) {
        _useLogScale = b;
    }

    public boolean getLogScaleEnabled() {
        return _useLogScale;
    }

    private static class PlayerInfo {
        public String name;
        public PlayerType type;

        public PlayerInfo(String name, PlayerType type) {
            this.name = name;
            this.type = type;
        }
    }
}