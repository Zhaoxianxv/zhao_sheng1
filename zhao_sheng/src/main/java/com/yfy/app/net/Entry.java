package com.yfy.app.net;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root
public class Entry  {
    @Namespace(reference = "http://schemas.microsoft.com/2003/10/Serialization/Arrays")
    private String string;

    public Entry(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
