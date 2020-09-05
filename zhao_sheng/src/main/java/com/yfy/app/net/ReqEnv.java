package com.yfy.app.net;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;


/**
 * 用户角色请求Envelope
 */

@Root(name = "soapenv:Envelope")
@NamespaceList({
        @Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soapenv"),
        @Namespace(reference = "http://schemas.microsoft.com/2003/10/Serialization/Arrays" ,prefix = "arr"),
        @Namespace(reference = TagFinal.NAMESPACE, prefix=TagFinal.TEM)
})

public class ReqEnv {
    @Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/")
    @Element(name = TagFinal.BODY, required = false)
    public ReqBody body;

    @Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/")
    @Element(name = "Header", required = false)
    public String aHeader="";

    private static Strategy strategy = new AnnotationStrategy();
    private static Serializer serializer = new Persister(strategy);
    @Override
    public String toString() {

        OutputStream out = new ByteArrayOutputStream();
        try {
            serializer.write(this, out);
            String result = out.toString();
            out.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }

    }

}
