package com.aleksey.combatradar.config;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.camel.CamelHusk;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.animal.equine.Donkey;
import net.minecraft.world.entity.animal.equine.Llama;
import net.minecraft.world.entity.animal.equine.Mule;
import net.minecraft.world.entity.animal.equine.TraderLlama;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.Ocelot;
import net.minecraft.world.entity.animal.fish.Cod;
import net.minecraft.world.entity.animal.fish.Pufferfish;
import net.minecraft.world.entity.animal.fish.Salmon;
import net.minecraft.world.entity.animal.fish.TropicalFish;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.golem.CopperGolem;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.entity.animal.nautilus.Nautilus;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilus;
import net.minecraft.world.entity.animal.panda.Panda;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.animal.squid.GlowSquid;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.animal.turtle.Turtle;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.entity.monster.illager.Illusioner;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.skeleton.Bogged;
import net.minecraft.world.entity.monster.skeleton.Parched;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.skeleton.Stray;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.entity.monster.zombie.Husk;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;

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

        _entityList.add(new RadarEntityInfo(Camel.class, "Camel", "icons/camel/camel.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Sniffer.class, "Sniffer", "icons/sniffer.png", GroupType.NEUTRAL));

        _entityList.add(new RadarEntityInfo(Armadillo.class, "Armadillo", "icons/armadillo.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Bogged.class, "Bogged", "icons/bogged.png", GroupType.AGGRESSIVE));
        _entityList.add(new RadarEntityInfo(Breeze.class, "Breeze", "icons/breeze.png", GroupType.AGGRESSIVE));

        _entityList.add(new RadarEntityInfo(Creaking.class, "Creaking", "icons/creaking.png", GroupType.AGGRESSIVE));

        _entityList.add(new RadarEntityInfo(HappyGhast.class, "Happy Ghast", "icons/ghast/happy_ghast.png", GroupType.NEUTRAL));

        _entityList.add(new RadarEntityInfo(CopperGolem.class, "Copper Golem", "icons/copper_golem.png", GroupType.NEUTRAL));

        _entityList.add(new RadarEntityInfo(CamelHusk.class, "Camel Husk", "icons/camel/camel_husk.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Nautilus.class, "Nautilus", "icons/nautilus/nautilus.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(ZombieNautilus.class, "Zombie Nautilus", "icons/nautilus/zombie_nautilus.png", GroupType.NEUTRAL));
        _entityList.add(new RadarEntityInfo(Parched.class, "Parched", "icons/parched.png", GroupType.AGGRESSIVE));

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

    public Identifier getEnabledIcon(Entity entity) {
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