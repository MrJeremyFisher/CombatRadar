package com.aleksey.combatradar.config;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.equine.SkeletonHorse;

public class RadarHorseInfo extends RadarEntityInfo {
    private static final String[] HORSE_VARIANTS = {"white", "creamy", "chestnut", "brown", "black", "gray", "darkbrown"};

    private final Identifier[] _icons;
    private final Identifier _skeletonHorse;

    public RadarHorseInfo(String name, String iconPath, GroupType groupType) {
        super(Horse.class, name, iconPath, groupType);

        addEntity(SkeletonHorse.class, null);

        _icons = new Identifier[HORSE_VARIANTS.length];
        for (int i = 0; i < HORSE_VARIANTS.length; i++)
            _icons[i] = Identifier.fromNamespaceAndPath("combatradar", "icons/horse/horse_" + HORSE_VARIANTS[i] + ".png");

        _skeletonHorse = Identifier.fromNamespaceAndPath("combatradar", "icons/horse/horse_skeleton.png");
    }

    @Override
    public Identifier getIcon(Entity entity) {
        if (entity == null)
            return super.getIcon(entity);

        if (entity instanceof SkeletonHorse)
            return _skeletonHorse;

        Horse horseEntity = (Horse) entity;
        int horseVariant = horseEntity.getVariant().ordinal() % 7;

        return _icons[horseVariant];
    }
}
