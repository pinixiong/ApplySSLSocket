package com.rj.applysslsocket;


public class LLItem {
    //目前有editText,button,toggleButton
    private String type;
    private String textsize;
    private String color;
    private String ip;
    private String name;
    private Boolean iseditText = false;
    private Boolean isbutton = false;
    private Boolean istoggleButton = false;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTextsize() {
        return textsize;
    }

    public void setTextsize(String textsize) {
        this.textsize = textsize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean getIseditText() {
        return iseditText;
    }

    public void setIseditText(Boolean iseditText) {
        this.iseditText = iseditText;
    }

    public Boolean getIstoggleButton() {
        return istoggleButton;
    }

    public void setIstoggleButton(Boolean istoggleButton) {
        this.istoggleButton = istoggleButton;
    }

    public Boolean getIsbutton() {
        return isbutton;
    }

    public void setIsbutton(Boolean isbutton) {
        this.isbutton = isbutton;
    }
}
