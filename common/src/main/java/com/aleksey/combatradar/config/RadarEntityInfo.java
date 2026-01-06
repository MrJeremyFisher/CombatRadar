package com.aleksey.combatradar.config;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Aleksey Terzi
 */
public class RadarEntityInfo {
    private final Map<String, Identifier> _entities;
    private final Identifier _defaultIcon;
    private final String _name;
    private final GroupType _groupType;
    private final String _entityClassName;
    private boolean _enabled;
    public RadarEntityInfo(Class<? extends Entity> entityClass, String name, String iconPath, GroupType groupType) {
        this(entityClass.getCanonicalName(), name, iconPath, groupType);
    }

    protected RadarEntityInfo(String entityClass, String name, String iconPath, GroupType groupType) {
        _name = name;
        _groupType = groupType;
        _enabled = true;
        _entityClassName = entityClass;

        _entities = new HashMap<>();
        _entities.put(entityClass, _defaultIcon = Identifier.fromNamespaceAndPath("combatradar", iconPath));
    }

    public String getName() {
        return _name;
    }

    public GroupType getGroupType() {
        return _groupType;
    }

    public String getEntityClassName() {
        return _entityClassName;
    }

    public Identifier getIcon(Entity entity) {
        if (entity == null || _entities.size() == 1)
            return _defaultIcon;

        var entityClassName = entity.getClass().getCanonicalName();

        return _entities.get(entityClassName);
    }

    public Identifier getIcon(String entity) {
        if (entity == null || _entities.size() == 1)
            return _defaultIcon;

        return _entities.get(entity);
    }

    public boolean getEnabled() {
        return _enabled;
    }

    public void setEnabled(boolean value) {
        _enabled = value;
    }

    public RadarEntityInfo addEntity(Class<? extends Entity> entityClass, String iconPath) {
        var icon = iconPath != null ? Identifier.fromNamespaceAndPath("combatradar", iconPath) : null;
        _entities.put(entityClass.getCanonicalName(), icon);
        return this;
    }

    public void addToMap(Map<String, RadarEntityInfo> map) {
        for (var entityClass : _entities.keySet())
            map.put(entityClass, this);
    }

    public static class EntityComparator implements Comparator<RadarEntityInfo> {
        @Override
        public int compare(RadarEntityInfo i1, RadarEntityInfo i2) {
            return i1._name.compareTo(i2._name);
        }
    }
}
