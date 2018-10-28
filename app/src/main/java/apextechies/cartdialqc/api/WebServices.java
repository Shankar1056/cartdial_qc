package apextechies.cartdialqc.api;

public class WebServices {

    public static final String BASEURL = "http://apextechies.com/cartdial_qc/index.php/";
    public static final String DEZENTSBASEURL = "https://cartdial.com/App_Data/index.php/";
    public static final String XAPIKEY = "1a!2b@3c#4d$5e%6f^";
    public static final String LOGIN = BASEURL+"user_login";
    public static final String USERDETAILS_BYORDERID = DEZENTSBASEURL+"get_user_detals";
    public static final String PRODUCTDETAILS_BYORDERID = DEZENTSBASEURL+"get_product_detals";
    public static final String GETQUESTION = BASEURL+"get_question";
    public static final String INSERTUPDATE_QCSTATUS = BASEURL+"insert_qc_statue";
    public static final String UPLOADIMAGE = DEZENTSBASEURL+"upload_topper_image";
    public static final String NEWORDERLIST = DEZENTSBASEURL+"get_all_order_list";
    public static final String DELIVERYBOYLIST = BASEURL+"get_delivery_boy_list";
    public static final String ASSIGNORDER = BASEURL+"assign_order";
    public static final String UPDATEORDERSTATUS = DEZENTSBASEURL+"update_order_status";
}
