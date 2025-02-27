package com.vgames.survivalreckoning.framework.entity;

import com.vgames.survivalreckoning.framework.entity.GameObject;
import com.vgames.survivalreckoning.framework.entity.NullComponent;
import com.vgames.survivalreckoning.framework.entity.component.Component;
import com.vgames.survivalreckoning.framework.log.Logger;
import com.vgames.survivalreckoning.framework.log.annotation.GenerateCriticalFile;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@GenerateCriticalFile
public abstract class ComponentContainer extends Logger {

    private List<Component> componentList;
    private GameObject owner;

    public ComponentContainer() {
        this.componentList = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T addComponent(Class<T> componentClass) {
        for(Component component : this.componentList) {
            if(component.getClass().isAssignableFrom(componentClass)) {
                return this.getComponent(componentClass);
            }
        }

        try {
            Component _component = componentClass.getDeclaredConstructor(GameObject.class).newInstance(this.owner);
            _component.setParent(this.owner);
            _component.start();
            this.componentList.add(_component);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            critical("", new RuntimeException(e));
        }

        return (T) NullComponent.get();
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for(Component component : this.componentList) {
            if(component.getClass().equals(componentClass)) {
                return (T) component;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T removeComponent(Class<T> componentClass) {
        for(Component component : this.componentList) {
            if(component.getClass().equals(componentClass)) {
                Component _component = getComponent(component.getClass());
                return (T) _component;
            }
        }

        return null;
    }

    public GameObject getOwner() {
        return owner;
    }

    protected void setOwner(GameObject owner) {
        this.owner = owner;
    }

    public void clearComponents() {
        for(Component component : this.componentList) {
            component.cleanup();
        }
    }

    public void updateComponents() {
        for(Component component : componentList) {
            component.update();
        }
    }
}
