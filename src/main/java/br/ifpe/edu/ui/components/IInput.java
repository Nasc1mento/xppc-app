package br.ifpe.edu.ui.components;

public interface IInput extends IComponent{
    String getStringValue();
    void addChangeListener(Runnable r);
    void clear();
}
