package com.yfy.net;
import android.util.Xml;

import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yfy1 on 2016/7/28.
 */
public class SoapAccessor {


    public final static String WCF_ERROR = "wcf_error";

    private volatile static SoapAccessor instance;

    private WcfConfiguration configuration;

    private static final String ERROR_CONFIG_WITH_NULL = "WcfAccessor configuration can not be null";

    private static final String ERROR_INIT_CONFIG_WITH_NULL = "WcfAccessor configuration can not be initialized with null";


    private static final String WARNING_RE_INIT_CONFIG = "Try to initialize WcfAccessor which had already been initialized before. ";


    private final static String WRAPPER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<SOAP-ENV:Envelope \n"
            + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" \n"
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n"
            + "xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" \n"
            + "SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" \n"
            + "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"> \n"
            + "<SOAP-ENV:Body>%@</SOAP-ENV:Body> \n" + "</SOAP-ENV:Envelope>";
    private final static String METHOD_WRAPPER = "<%% xmlns:arr=\"http://schemas.microsoft.com/2003/10/Serialization/Arrays\" xmlns=\"http://tempuri.org/\">@@</%%>";

    private SoapAccessor() {
    }

    public static SoapAccessor getInstance() {
        if (instance == null) {
            synchronized (SoapAccessor.class) {
                if (instance == null) {
                    instance = new SoapAccessor();
                }
            }
        }
        return instance;
    }


    public synchronized void init(WcfConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
        }
        if (this.configuration == null) {
            this.configuration = configuration;
        } else {
            Logger.e(TagFinal.ZXX, WARNING_RE_INIT_CONFIG);
        }

    }


    public synchronized void unInit() {
        configuration = null;
    }


    public static String string2Json(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }


    public String loadResult(Object[] params, String method)throws MalformedURLException, IOException, JSONException,XmlPullParserException {
        Logger.e( "loadResult:1 ");
        if (configuration == null) {
            throw new IllegalArgumentException(ERROR_CONFIG_WITH_NULL);
        }
        return loadResult(getSoapXml(params, method), method);
    }

    private String loadResult(String soapXml, String method) throws IOException, MalformedURLException, XmlPullParserException {
        byte[] entry = soapXml.getBytes();
        HttpURLConnection conn = (HttpURLConnection) new URL(configuration.url).openConnection();
        conn.setConnectTimeout(configuration.time_out);
        conn.setReadTimeout(2*1000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
        conn.setRequestProperty("Content-Length", String.valueOf(entry.length));
        conn.setRequestProperty("SOAPAction", configuration.soap_action+ method);
        conn.getOutputStream().write(entry);
        String result = WCF_ERROR;
        Logger.e( "getSoapXml: "+soapXml );
        if (conn.getResponseCode() == 200) {
            result = parseResponseXML(conn.getInputStream(), method);
        } else {
            Logger.e("zxx","conn.getResponseCode()--"+conn.getResponseCode());
        }
        conn.disconnect();
        return result;
    }



    private static String parseResponseXML(InputStream inStream, String method) throws XmlPullParserException, IOException
    {
        StringBuffer out = new StringBuffer();
        out.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        byte b[] = new byte[10240];
        int len=0;
        while ((len=inStream.read(b))!=-1){
            out.append(new String(b, 0, len,"utf-8"));
        }
        inStream = new ByteArrayInputStream(out.toString().getBytes("utf-8"));

        String reponse = method + "Result";
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inStream, "UTF-8");
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    String name = parser.getName();
                    if (reponse.equals(name)) {
                        String result = parser.nextText();
                        return result;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return null;
    }


    private String getSoapXml(Object[] parameters, String method) throws JSONException
    {
        LinkedHashMap<String, String> hashMap = serviceJsonParser(method,configuration.wcf_json);
        String methodXml = METHOD_WRAPPER
                .replace("%%", method)
                .replace("@@",getParamsXml(hashMap, parameters));
        String soapXml = WRAPPER.replace("%@", methodXml);

        return soapXml;
    }


    private LinkedHashMap<String, String> serviceJsonParser(String methodName, String wcf_json)
    {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        try {
            JSONObject obj = new JSONObject(wcf_json);
            JSONArray paramArray = obj.getJSONArray(methodName);
            for (int i = 0; i < paramArray.length(); i++) {
                JSONObject paramObj = paramArray.getJSONObject(i);
                String paramType = paramObj.getString("paramType");
                String paramName = paramObj.getString("paramName");
//                System.out.println(paramName + "--------" + paramType);
                map.put(paramName, paramType);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    private String getParamsXml(LinkedHashMap<String, String> hashMap, Object[] parameters)
    {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, String> entry : hashMap.entrySet())
        {
            sb.append("<")
                    .append(entry.getKey())
                    .append(">")
                    .append(parameters[i])
                    .append("</")
                    .append(entry.getKey())
                    .append(">");
            if (i==0){
            }

            i++;
        }
        String resut = sb.toString();
        return resut;
    }

    public static class WcfConfiguration {
        public String nameSpace;
        public String url;
        public String soap_action;
        public int time_out;
        public String wcf_json;

        public WcfConfiguration(String nameSpace, String url, String soap_action, int time_out, String wcf_json) {
            this.nameSpace = nameSpace;
            this.url = url;
            this.soap_action = soap_action;
            this.time_out = time_out;
            this.wcf_json = wcf_json;
        }
    }

    public Object getWcfConfiguration() {
        return configuration;
    }



//
//    public final static String WCF_ERROR = "wcf_error";
//
//    public String loadResult(String soapXml, String method) throws IOException, MalformedURLException, XmlPullParserException {
//        byte[] entry = soapXml.getBytes();
//        HttpURLConnection conn = (HttpURLConnection) new URL(TagFinal.CHENGDU_SHIYAN).openConnection();
//        Logger.e(TagFinal.ZXX, "loadResult: 3" );
//        conn.setConnectTimeout(TagFinal.TIME_OUT);
//        conn.setRequestMethod("POST");
//        conn.setDoOutput(true);
//        conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
//        conn.setRequestProperty("Content-Length", String.valueOf(entry.length));
//        conn.setRequestProperty("SOAPAction",  TagFinal.NET_SOAP_ACTION+ method);
//        conn.getOutputStream().write(entry);
//        String result = WCF_ERROR;
//        if (conn.getResponseCode() == 200) {
//            result = parseResponseXML(conn.getInputStream(), method);
//        } else {
//            Logger.e("zxx","conn.getResponseCode()--"+conn.getResponseCode());
//        }
//        conn.disconnect();
//        return result;
//    }
//
//
//
//    private static String parseResponseXML(InputStream inStream, String method) throws XmlPullParserException, IOException
//    {
//        StringBuffer out = new StringBuffer();
//        out.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
//        byte[] b = new byte[4096];
//        for (int n; (n = inStream.read(b)) != -1;) {
//            out.append(new String(b, 0, n));
//        }
//        inStream = new ByteArrayInputStream(out.toString().getBytes("UTF-8"));
//
//        String reponse = method + "Result";
//        XmlPullParser parser = Xml.newPullParser();
//        parser.setInput(inStream, "UTF-8");
//        int eventType = parser.getEventType();
//        while (eventType != XmlPullParser.END_DOCUMENT) {
//
//            switch (eventType) {
//                case XmlPullParser.START_TAG:
//                    String name = parser.getName();
//
//                    if (reponse.equals(name)) {
//                        String result = parser.nextText();
//                        return result;
//                    }
//                    break;
//            }
//            eventType = parser.next();
//        }
//        return null;
//    }
//
//
//
//
//
//    private StringBuilder getOneKey(StringBuilder sb ,String key,String value,boolean is_one,ArrayList s,int type){
//        String one="<",two=">",three="</";
//        if (is_one){
//            sb.append(one)
//                    .append(key)
//                    .append(two)
//                    .append(value)
//                    .append(three)
//                    .append(key)
//                    .append(two);
//        }else{
//            sb.append(one)
//                    .append(key)
//                    .append(two)
//                    .append(getThree(s, type))
//                    .append(three)
//                    .append(key)
//                    .append(two);
//        }
//        return sb;
//    }
//    private String getTwoKey(ArrayList s,int type){
//        StringBuilder sb = new StringBuilder();
//        String one="<",two=">",three="</",five="int",sex="string",args="ArrayOfstring",argi="ArrayOfint";
//        String tp;
//        String arg;
//        if (type==1){
//            tp=five;
//            arg=argi;
//        }else{
//            tp=sex;
//            arg=args;
//        }
//        sb.append(one).append(arg).append(two);
//        for (int index=0;index<s.size();index++){
//            sb.append(one)
//                    .append(tp)
//                    .append(two)
//                    .append(s.get(index))
//                    .append(three)
//                    .append(tp)
//                    .append(two);
//        }
//        sb.append(three).append(arg).append(two);
//        return sb.toString();
//    }
//    @SuppressLint("HandlerLeak")
//    Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case -1:
//                    Toast.makeText(DutyAddActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
//                   dismissProgressDialog();
//                    break;
//                case 0:
//                    Toast.makeText(DutyAddActivity.this, "网络原因，验证失败", Toast.LENGTH_SHORT).show();
//                    dismissProgressDialog();
//                    break;
//                case -2:
//                    Toast.makeText(DutyAddActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
//                    dismissProgressDialog();
//                    break;
//                case 1:
//                    //成功
//                    break;
//            }
//        };
//    };


}

