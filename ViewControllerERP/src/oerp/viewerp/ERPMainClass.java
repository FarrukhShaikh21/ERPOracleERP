package oerp.viewerp;

//import erpglobals.viewglobals.ERPGlobalsClass;

import erpglobals.modelglobals.ERPGlobalPLSQLClass;
import erpglobals.modelglobals.ERPUserAttribute;

import erpglobals.viewglobals.ERPGlobalsClass;

import java.io.IOException;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.faces.application.FacesMessage;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.binding.BindingContainer;

import oracle.jbo.server.DBTransaction;


public class ERPMainClass {
    public ERPMainClass() {
        super();
    }
    private Integer ERPuserId;
    private String ERPuserCode = "";
    private ERPUserAttribute ERPUserAttributes;
    String ERPuserPassword;
    private Integer ERPGlobalDefaultComp;
    Integer attempt = 0;
    private String ERPIsPwdExpired="ERPNO";
    private RichPopup ERPForgotPopup;
    private RichPopup ERPUserIdPopup;
    private RichPopup ERPRecoveryMethodPopup;
    private RichPopup ERPEnterVerification;
    private String ERPStringRecoverVia;
    private String ERPRecoveryType;
    private String ERPVerifyUserId;
    private DBTransaction ERPdbt;
    private String ERPStrVerificationCode;
    private String erpConnectType   ;
    private String erpUserName;
    private String erpEmail;
    private String erpMobile;
    private String erpPLSQL;
    private RichPopup ERPVerifyByEmailMobile;
    private String erpRecoverUserPassword="P";// this will store P(Password), U(User id)
    private String erpStrEmailMobile;
    private String erpValueType="M";// this will store M(Mobile), E(Email)



    public void setErpValueType(String erpValueType) {
        this.erpValueType = erpValueType;
    }

    public String getErpValueType() {
        return erpValueType;
    }

    public void setErpStrEmailMobile(String erpStrEmailMobile) {
        this.erpStrEmailMobile = erpStrEmailMobile;
    }

    public String getErpStrEmailMobile() {
        return erpStrEmailMobile;
    }

    public void setERPVerifyByEmailMobile(RichPopup ERPVerifyByEmailMobile) {
        this.ERPVerifyByEmailMobile = ERPVerifyByEmailMobile;
    }

    public RichPopup getERPVerifyByEmailMobile() {
        return ERPVerifyByEmailMobile;
    }

    public void setERPdbt(DBTransaction ERPdbt) {
        this.ERPdbt = ERPdbt;
    }

    public DBTransaction getERPdbt() {
        return ERPdbt;
    }

    public void setErpConnectType(String erpConnectType) {
        this.erpConnectType = erpConnectType;
    }

    public String getErpConnectType() {
        return erpConnectType;
    }

    public void setErpUserName(String erpUserName) {
        this.erpUserName = erpUserName;
    }

    public String getErpUserName() {
        return erpUserName;
    }

    public void setErpEmail(String erpEmail) {
        this.erpEmail = erpEmail;
    }

    public String getErpEmail() {
        return erpEmail;
    }

    public void setErpMobile(String erpMobile) {
        this.erpMobile = erpMobile;
    }

    public String getErpMobile() {
        return erpMobile;
    }

    public void setERPUserAttributes(ERPUserAttribute ERPUserAttributes) {
        this.ERPUserAttributes = ERPUserAttributes;
        //System.out.println("setERPUserAttributes+code"+this.ERPUserAttributes.getUserCode());
        System.out.println("setERPUserAttributes+code");

    }

    public ERPUserAttribute getERPUserAttributes() {
        return ERPUserAttributes;
    }

    public void setERPGlobalDefaultComp(Integer ERPGlobalDefaultComp) {
        this.ERPGlobalDefaultComp = ERPGlobalDefaultComp;
    }

    public Integer getERPGlobalDefaultComp() {
        return ERPGlobalDefaultComp;
    }

    public void setERPIsPwdExpired(String ERPIsPwdExpired) {
        this.ERPIsPwdExpired = ERPIsPwdExpired;
    }

    public String getERPIsPwdExpired() {
       // System.out.println("this is system:"+ERPIsPwdExpired);
        return ERPIsPwdExpired;
    }

    public void setERPuserId(Integer ERPuserId) {
        this.ERPuserId = ERPuserId;
    }

    public Integer getERPuserId() {
        return ERPuserId;
    }

    public void setERPuserCode(String ERPuserCode) {
        this.ERPuserCode = (""+ERPuserCode).toUpperCase();
        //System.out.println("this.ERPuserCode"+this.ERPuserCode);
    }

    public String getERPuserCode() {
        return ERPuserCode;
    }

    public void setERPuserPassword(String ERPuserPassword) {
        this.ERPuserPassword = ERPuserPassword;
    }

    public String getERPuserPassword() {
        return ERPuserPassword;
    }

    public String doLoginERPOracle() {
        BindingContainer bc = ERPGlobalsClass.doGetERPBindings();
        //BindingContainer bc = BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding ib = (DCIteratorBinding) bc.get("SysUsersROIterator");
        DBTransaction dbt = (DBTransaction) ib.getViewObject().getApplicationModule().getTransaction();
        //System.out.println(getERPuserCode());
        //System.out.println(getERPuserPassword());
        Integer ErpCurExpDateComp=0;
        ERPUserAttribute erpua=ERPGlobalsClass.doFuncCheckErpLoginView(dbt, getERPuserCode(), getERPuserPassword());
        Date ERPDate=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");  
        String erpCurDate=formatter.format(ERPDate);
        String erpExpDate=erpua.getExpireDate();
        SimpleDateFormat erpsdf=new SimpleDateFormat();
        ERPIsPwdExpired="ERPNO";
       // System.out.println(formatter.format(ERPDate)+ " CURENT DATE"); 
       // System.out.println(erpua.getExpireDate() + "expirydate");

        try {
            ErpCurExpDateComp=formatter.parse(erpExpDate).compareTo(formatter.parse(erpCurDate));
            
        } catch (Exception e) {
            ErpCurExpDateComp=0;
        }
        //System.out.println("calling::"+erpua.getErpLoginStatus());
        //System.out.println(erpua.getErpLoginStatus());
        if (erpua.getErpLoginStatus().equals("ERPNO")) {
            //System.out.println(" invalid password");
            ERPGlobalsClass.doShowERPMessage("Authentication Fails, You have entered an invalid User ID or Password", FacesMessage.SEVERITY_INFO);
            return null;
       } 
        if (erpua.getIsLock().equals("Y")) {
           ERPGlobalsClass.doShowERPMessage("Your account is lock.", FacesMessage.SEVERITY_INFO);            
           return null;
       }
        else if (erpua.getErpIsPwdExpired().equals("Y") || ErpCurExpDateComp<=0) {
           setERPIsPwdExpired("ERPYES");
           //System.out.println("password is expired... pls ");
           //ERPGlobalsClass.doShowERPMessage("Your password is expired.", FacesMessage.SEVERITY_INFO);            
          // return null;
       }
        //System.out.println("one");
        setERPuserId(erpua.getUserId());
        setERPuserCode(erpua.getUserCode());        
        setERPGlobalDefaultComp(erpua.getErpDefGloalCompany()); 
        setERPUserAttributes(erpua);
        //0       System.out.println("two");
        //System.out.println(erpua.getErpDefGloalCompany()+ "def company");
        //System.out.println(erpua.getUserCode()+ "glob user code");
        return "ERP-ACT-LOGIN-APP";
    }
    
    
    public String doLoginERPOracle_OLD() {
        BindingContainer bc = ERPGlobalsClass.doGetERPBindings();
        //BindingContainer bc = BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding ib = (DCIteratorBinding) bc.get("SysUsersROIterator");
        DBTransaction dbt = (DBTransaction) ib.getViewObject().getApplicationModule().getTransaction();
        /*String plsql =
            " begin ?:=FUNC_CHECK_ERP_LOGIN('" + getERPuserCode() + "','" + getERPuserPassword() +
            "'); end;";
        */
        //ERPGlobalsClass.
        //ERPGlobalsClass.doFuncCheckErpLoginView(d, arg1, arg2)
        String plsql="{ ? = call func_check_erp_login('"+getERPuserCode()+"','"+getERPuserPassword()+"')}";
         CallableStatement cs = dbt.createCallableStatement(plsql, 1);
        try {
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.executeUpdate();
            String result = cs.getString(1);
            if (!result.equals("ERPYES")) {
                ERPGlobalsClass.doShowERPMessage(result, FacesMessage.SEVERITY_INFO);
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(result));
                return null;
            }
            else if (result.equals("ERPEXP")) {
               //null;
           }
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
            try {
                cs.close();
                return null;
            } catch (SQLException e) {
            }
        }
        finally{
            try {
                cs.close();
            } catch (SQLException e) {
            }    
        }
        return "ERP-ACT-LOGIN-APP";
    } 
    public void doSetERPApplicationGlobals() {
       // System.out.println("doSetERPApplicationGlobals");
        System.out.println("i am calling:doSetERPApplicationGlobals");
        //ERPGlobalsClass.doSetErpGlobals(getERPUserAttributes());
        
        //ERPGlobalsClass.dos
    }

    public void doSetMainErpApplicationGlobals() {
       /*
        ADFContext.getCurrent().getPageFlowScope().put("G_USER_SNO",1);
        ADFContext.getCurrent().getPageFlowScope().put("G_USER_CODE","FARRUKH");
        ADFContext.getCurrent().getPageFlowScope().put("G_GLOBAL_DEF_COMPANY",1);
        ADFContext.getCurrent().getPageFlowScope().put("G_ERP_MODULE_ACTION","SEC_0008");
        ADFContext.getCurrent().getPageFlowScope().put("G_TEMP_COMP_ACCESS_TABLE","temp_admin_company_access");
        ADFContext.getCurrent().getPageFlowScope().put("G_USER_NAME","FARRUKH SHAIKH");
        ADFContext.getCurrent().getPageFlowScope().put("G_USER_PICTURE",null);
        
      // getERPUserAttributes().setUserCode(getERPuserCode());
       //getERPUserAttributes().setUserId(getERPuserId());
       
        setERPGlobalDefaultComp(1);
        if (1==1) {
            return;
       }*/
        System.out.println("one erp");
        ADFContext.getCurrent().getPageFlowScope().put("G_USER_SNO",getERPUserAttributes().getUserId());
        System.out.println("two erp");
        ADFContext.getCurrent().getPageFlowScope().put("G_USER_CODE",getERPUserAttributes().getUserCode());
        System.out.println("three erp");
        ADFContext.getCurrent().getPageFlowScope().put("G_GLOBAL_DEF_COMPANY",getERPUserAttributes().getErpDefGloalCompany());
        setERPGlobalDefaultComp(getERPUserAttributes().getErpDefGloalCompany());
        System.out.println("four erp");
        ADFContext.getCurrent().getPageFlowScope().put("G_TEMP_COMP_ACCESS_TABLE",getERPUserAttributes().getErpTempCompanyAccessTable());
        ADFContext.getCurrent().getPageFlowScope().put("G_USER_NAME",getERPUserAttributes().getERPUserName());
        //System.out.println("five erp");
        getERPUserAttributes().setUserCode(getERPuserCode());
        getERPUserAttributes().setUserId(getERPuserId());
        ADFContext.getCurrent().getPageFlowScope().put("G_USER_PICTURE",getERPUserAttributes().getERPUserPicture());
        //System.out.println("six erp-01"+getERPuserId());
        
    }


    public void setERPForgotPopup(RichPopup ERPForgotPopup) {
        this.ERPForgotPopup = ERPForgotPopup;
    }

    public RichPopup getERPForgotPopup() {
        return ERPForgotPopup;
    }


    public void setERPUserIdPopup(RichPopup ERPUserIdPopup) {
        this.ERPUserIdPopup = ERPUserIdPopup;
    }

    public RichPopup getERPUserIdPopup() {
        return ERPUserIdPopup;
    }


    public void setERPRecoveryMethodPopup(RichPopup ERPRecoveryMethodPopup) {
        this.ERPRecoveryMethodPopup = ERPRecoveryMethodPopup;
    }

    public RichPopup getERPRecoveryMethodPopup() {
        return ERPRecoveryMethodPopup;
    }


    public void setERPEnterVerification(RichPopup ERPEnterVerification) {
        this.ERPEnterVerification = ERPEnterVerification;
    }

    public RichPopup getERPEnterVerification() {
        return ERPEnterVerification;
    }


    public String ERPdoHideForgotPopup() {
        getERPForgotPopup().hide();
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        if (getErpRecoverUserPassword().equals("U")) {
            getERPVerifyByEmailMobile().show(hints);
        }
        else {
            getERPUserIdPopup().show(hints);
        }
        return null;
    }
  
    public String ERPdoHideUseridPopup() {
        ERPuserId=null;
        BindingContainer bc = ERPGlobalsClass.doGetERPBindings();
        //BindingContainer bc = BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding ib = (DCIteratorBinding) bc.get("SysUsersROIterator");
        ERPdbt = (DBTransaction) ib.getViewObject().getApplicationModule().getTransaction();
        String erpStr=ERPGlobalsClass.doExecuteSQLQuery(ERPdbt, "select concat(CONCAT( concat( concat(concat(s.user_id,'#01#'), s.user_name)  ,concat('#02#', coalesce(email,'-') ) ),'#03#'), coalesce(s.mobile_no,'-')) STR FROM SYS_USERS s  WHERE User_Code=upper('"+getERPVerifyUserId()+"')");
        
        if (erpStr.equals("-")) {
            ERPGlobalsClass.doShowERPMessage("This Login ID doesn't Exist in system.", FacesMessage.SEVERITY_ERROR);
            return null;
        }
        System.out.println(erpStr+ " erp str");
        String erpUserid=erpStr.substring(0, erpStr.indexOf("#01#") );
        setErpUserName(erpStr.substring(erpStr.indexOf("#01#")+4, erpStr.indexOf("#02#") ));
        setErpEmail(erpStr.substring(erpStr.indexOf("#02#")+4, erpStr.indexOf("#03#") ));
        setErpMobile(erpStr.substring(erpStr.indexOf("#03#")+4));
        
        
        //              ErpDoSendEmail((DBTransaction)am.getTransaction(),null,null,new String[][]{{"FARRUKH SHAIKH","mefarrukh@hotmail.com"},{"FARRUKH SHAIKH GMAIL","me.farrukhshaikh@gmail.com"}},null,null,"THIS IS TEST EMAIL-CC","THIS IS MY EMAIL <b>TEXT</b>",null,null,null);
        setERPuserId( Integer.parseInt(erpUserid) );
        
        getERPUserIdPopup().hide();
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        getERPRecoveryMethodPopup().show(hints);                
        return null;
    } 
    
    public String ERPdoHideEmailMobilePopup() {
        BindingContainer bc = ERPGlobalsClass.doGetERPBindings();
        //BindingContainer bc = BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding ib = (DCIteratorBinding) bc.get("SysUsersROIterator");
        ERPdbt = (DBTransaction) ib.getViewObject().getApplicationModule().getTransaction();
        String erpStr=ERPGlobalsClass.doExecuteSQLQuery(ERPdbt, "select concat(concat(concat(s.USER_ID,'#01#'), s.USER_CODE),concat('#02#',s.user_name)) AS USERNAME FROM SYS_USERS s  WHERE (lower(s.email)=lower('"+getErpStrEmailMobile()+"') or lower(s.mobile_no)=lower('"+getErpStrEmailMobile()+"'))");
        
        
        if (erpStr.equals("-")) {
            ERPGlobalsClass.doShowERPMessage("This Email/Mobile doesn't Exist in system.", FacesMessage.SEVERITY_ERROR);
            return null;
        }
        
        //concat(CONCAT( concat( concat(concat(s.user_id,'#01#'), s.user_name)  ,concat('#02#', coalesce(email,'-') ) ),'#03#'), coalesce(s.mobile_no,'-'))
            
        String erpUserId=erpStr.substring(0, erpStr.indexOf("#01#") );
        setERPuserId(Integer.parseInt(erpUserId));
        setERPuserCode(erpStr.substring(erpStr.indexOf("#01#") + 4, erpStr.indexOf("#02#")));
        setErpUserName(erpStr.substring(erpStr.indexOf("#02#") + 4));


        System.out.println(erpUserId + " erpUserCode "+ getERPuserCode()+" erp user code"+ getErpUserName());

        System.out.println("user recovery");
        
        if (getErpStrEmailMobile().contains("@")) {
           setErpEmail(getErpStrEmailMobile());
           setErpUserName(erpStr);
           setErpValueType("E");
       }
        else {
            setErpMobile(getErpStrEmailMobile()); 
            setErpValueType("M");
        }
        
        getERPVerifyByEmailMobile().hide();
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        getERPRecoveryMethodPopup().show(hints);                
        return null;
    } 
    public String ERPdoHideRecoveryPopup() {
        //String erpQuery="";
        erpPLSQL="";
        erpConnectType=ERPGlobalPLSQLClass.doErpGetConnTypeModel(ERPdbt);
        String isSuccessfull="N";
        Integer ErpVerivicationCode=null;
        while (isSuccessfull.equals("N")) {//while 
            // the purpose of this loop is same verification code can be generated from other session
            // in database we have unique constraint of (date,verification_code)
            try{  
            //generating random verification code
            ErpVerivicationCode = (int) (Math.random() * (999999 - 100001 + 1) + 100001);
            //ErpVerivicationCode = ERPGlobalsClass.doExecuteSQLQuery(ERPdbt, erpQuery); //for getting random number
            if (erpConnectType.equals("ERPORACLE")) {
                ///if connection type is oracle then system will insert in table
                erpPLSQL =
                    "begin update sys_veritication_codes cod set cod.is_expired='Y' where cod.user_sno="+getERPuserId()+"; " +
                    "insert into sys_veritication_codes (veritication_codes_sno,verification_code,verification_date,user_sno,created_date,expired_date,is_expired,is_used) values " +
                    " (SEQ_SYS_VERITICATION_CODES.Nextval," + ErpVerivicationCode + ", trunc(sysdate),"+getERPuserId()+",sysdate,SYSDATE + (5/1440),'N','N'); commit; end;";
            } else {
                ///if connection type is MYSQL then system will insert in table
                ERPGlobalPLSQLClass.doErpExecutePLSQLModel(ERPdbt, "update sys_veritication_codes cod set cod.is_expired='Y' where cod.user_sno="+getERPuserId(), "Y");
                erpPLSQL ="insert into sys_veritication_codes (verification_code,verification_date,user_sno,created_date,is_expired,expired_date,is_used) " +
                    "values (" + ErpVerivicationCode + ", SYSDATE()," + getERPuserId() + ",SYSDATE(),'N',date_add(sysdate(),interval 5 minute),'N')";
            }
            ERPGlobalPLSQLClass.doErpExecutePLSQLModel(ERPdbt, erpPLSQL, "Y");
            isSuccessfull="Y";//to exit loop
           }
            catch(Exception ex) {//in case of exception then rollback
                System.out.println(ex.getMessage());
                ERPdbt.rollback();
            }
        }
        if (getERPRecoveryType().equals("ERPEMAIL")) {
           ERPGlobalsClass.ErpDoSendEmail(ERPdbt, null, null, new String[][]{{getErpUserName(),getErpEmail()}}, null, null/*bcc*/, "Verification Code", " Your Verifivation code is <b>"+ ErpVerivicationCode +"</b>. Do not share with anyone.", null, null, null);
       }
        else if (getERPRecoveryType().equals("ERPSMS")) {
            //ERPdoSendSMS((DBTransaction)am.getTransaction(), null, null, null, null, "923337116635", null, "hello message", null);
            try {
                ERPGlobalsClass.ERPdoSendSMS(ERPdbt, null, null, null, null, getErpMobile(), null,
                                             "Your Verifivation code is " + ErpVerivicationCode +
                                             ". Do not share with anyone.", null);
            } catch (IOException e) {
                ERPGlobalsClass.doShowERPMessage("Unable To Send Message. Please contact Administrator.", FacesMessage.SEVERITY_ERROR);
                return null;
            }
        }
        getERPRecoveryMethodPopup().hide();
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        getERPEnterVerification().show(hints);                
        return null;
    } 

   public String ERPdoHideVerificationPopup() {
       String erpSysPassword=null;
       if (getErpConnectType().equals("ERPMYSQL")) {//for mysql only
            getERPdbt().rollback();
       }
       String ErpVerivicationCode = ERPGlobalsClass.doExecuteSQLQuery(ERPdbt, "SELECT verification_code FROM sys_veritication_codes where is_expired='N' and expired_date > "+(getErpConnectType().equals("ERPORACLE")?" sysdate ":" sysdate() ")+" and user_sno="+getERPuserId()); //for getting random number
       
       if (!ErpVerivicationCode.equals(getERPStrVerificationCode())) {
        ERPGlobalsClass.doShowERPMessage("Invalid/Expired Verification Code", FacesMessage.SEVERITY_ERROR);
        return null;
       }
       
       //if (getErpRecoverUserPassword().equals("P")) {
           erpPLSQL="update sys_veritication_codes cod set cod.is_expired='Y',is_used='Y', used_date= "+(getErpConnectType().equals("ERPORACLE")?" sysdate ":" sysdate() ")+" where cod.user_sno=" + getERPuserId();
       //}
        
       // if (getErpConnectType().equals("ERPORACLE")&& getErpRecoverUserPassword().equals("P")) {
            erpPLSQL ="begin "+erpPLSQL +"; commit; end;";
                
        //}
        //if(getErpRecoverUserPassword().equals("P")){
        ERPGlobalPLSQLClass.doErpExecutePLSQLModel(ERPdbt, erpPLSQL, "Y");
        //}
        
        getERPEnterVerification().hide();
        erpSysPassword=ERPGlobalsClass.erpDoGetForceAlphaNumPass(8);
        if (getERPRecoveryType().equals("ERPEMAIL") && getErpRecoverUserPassword().equals("P")) {
           ERPGlobalsClass.ErpDoSendEmail(ERPdbt, null, null, new String[][]{{getErpUserName(),getErpEmail()}}, null, null/*bcc*/, "Verification Code", " Your system generated password is <b>"+ erpSysPassword +"</b>. You will be asked to change the password after login.", null, null, null);
        }
        else if (getERPRecoveryType().equals("ERPSMS") && getErpRecoverUserPassword().equals("P")){
            try {
                ERPGlobalsClass.ERPdoSendSMS(ERPdbt, null, null, null, null, getErpMobile(), null," Your system generated password is <b>"+ erpSysPassword +"</b>. You will be asked to change the password after login.", null);
            } catch (IOException e) {
                ERPGlobalsClass.doShowERPMessage("Unable To Send Message. Please contact Administrator.", FacesMessage.SEVERITY_ERROR);
            }
        }
        //////////////
        if (getERPRecoveryType().equals("ERPEMAIL") && getErpRecoverUserPassword().equals("U")) {
           ERPGlobalsClass.ErpDoSendEmail(ERPdbt, null, null, new String[][]{{getErpUserName(),getErpEmail()}}, null, null/*bcc*/, "Login ID", " Your Login ID is <b>"+ getERPuserCode() +"<b>.", null, null, null);
        }
        else if (getERPRecoveryType().equals("ERPSMS") && getErpRecoverUserPassword().equals("U")){
            try {
                ERPGlobalsClass.ERPdoSendSMS(ERPdbt, null, null, null, null, getErpMobile(), null," Your Login ID is "+ getERPuserCode() +".", null);
            } catch (IOException e) {
                ERPGlobalsClass.doShowERPMessage("Unable To Send Message. Please contact Administrator.", FacesMessage.SEVERITY_ERROR);
            }
        }
        ////////////
        if (erpConnectType.equals("ERPORACLE") && getErpRecoverUserPassword().equals("P"))
            {
            erpPLSQL =
            " begin update sys_users s " + "set is_expired='N',expiry_date=trunc(sysdate) where user_id=" +getERPuserId() + ";" +
            " update sys_user_password set is_active='N' where user_id="+getERPuserId()+";"+
            " insert into sys_user_password(user_password_sno,passwordd,password_date,IS_ACTIVE,user_id)"+" \n"+
            " values (seq_sys_user_password.nextval,DBMS_CRYPTO.encrypt (UTL_I18N.string_to_raw ('" + erpSysPassword+"', 'AL32UTF8'),(6+ 256+ 4096),UTL_I18N.string_to_raw ('$rPl0G!NK$Ysyste', 'AL32UTF8')),sysdate,'Y',"+getERPuserId()+"); "+
            " insert into sys_user_password_history(PASSWORD_HISTORY_SNO,USER_ID,USER_PASSWORD,CHANGE_DATE)" +
            " values(seq_sys_user_password_history.nextval,"+getERPuserId()+","+
            " DBMS_CRYPTO.encrypt (UTL_I18N.string_to_raw ('" +erpSysPassword +"', 'AL32UTF8'),(6+ 256+ 4096),UTL_I18N.string_to_raw ('$rPl0G!NK$Ysyste', 'AL32UTF8')),sysdate);"+
            " commit; end;";
            ERPGlobalPLSQLClass.doErpExecutePLSQLModel(ERPdbt, erpPLSQL,"Y");
        }
            else if(!erpConnectType.equals("ERPORACLE") && getErpRecoverUserPassword().equals("P")) {
            erpPLSQL =
                " update sys_users s " + "set is_expired='N',expiry_date=sysdate() where user_id=" + getERPuserId() +
                ";";
            ERPGlobalPLSQLClass.doErpExecutePLSQLModel(ERPdbt, erpPLSQL, "Y");
            erpPLSQL = " update sys_user_password set is_active='N' where  is_active='Y' and user_id=" + getERPuserId() + ";";
            ERPGlobalPLSQLClass.doErpExecutePLSQLModel(ERPdbt, erpPLSQL, "Y");
            erpPLSQL =
                "insert into sys_user_password(user_id,passwordd,password_date,is_active) " + " values(" +
                getERPuserId() + ", AES_ENCRYPT('" + erpSysPassword + "','$rPl0G!NK$Ysyste'),sysdate(),'Y');";
            ERPGlobalPLSQLClass.doErpExecutePLSQLModel(ERPdbt, erpPLSQL, "Y");
            erpPLSQL =
                " insert into sys_user_password_history(user_id,user_password,change_date) " + "values (" +
                getERPuserId() + ",AES_ENCRYPT('" + erpSysPassword + "','$rPl0G!NK$Ysyste'), sysdate())";
            ERPGlobalPLSQLClass.doErpExecutePLSQLModel(ERPdbt, erpPLSQL, "Y");
        }
           
        setERPuserId(null);
        if (getERPRecoveryType().equals("ERPEMAIL") && getErpRecoverUserPassword().equals("P")) {
           ERPGlobalsClass.doShowERPMessage("Password has been sent to your Email ("+getErpEmail().toLowerCase()+").", FacesMessage.SEVERITY_ERROR);  
       }
        else if (getERPRecoveryType().equals("ERPSMS") && getErpRecoverUserPassword().equals("P")) {
                ERPGlobalsClass.doShowERPMessage("Password has been sent to your Mobile No ("+getErpMobile()+").", FacesMessage.SEVERITY_ERROR);  
        }
       
        else if (getErpRecoverUserPassword().equals("U")) {
            ERPGlobalsClass.doShowERPMessage("User ID has been sent to your "+(getErpValueType().equals("M")?" Mobile ":"Email" )+ " ("+(getErpValueType().equals("M")?getErpMobile():getErpEmail().toLowerCase())+").", FacesMessage.SEVERITY_ERROR);  
        }
         
        return null;
    } 

    public void setERPStringRecoverVia(String ERPStringRecoverVia) {
        this.ERPStringRecoverVia = ERPStringRecoverVia;
    }

    public String getERPStringRecoverVia() {
        return ERPStringRecoverVia;
    }

    public void setERPVerifyUserId(String ERPVerifyUserId) {
        this.ERPVerifyUserId = ERPVerifyUserId;
    }

    public String getERPVerifyUserId() {
        return ERPVerifyUserId;
    }

    public void setERPRecoveryType(String ERPRecoveryType) {
        this.ERPRecoveryType = ERPRecoveryType;
    }

    public String getERPRecoveryType() {
        return ERPRecoveryType;
    }

    
    public void setERPStrVerificationCode(String ERPStrVerificationCode) {
        this.ERPStrVerificationCode = ERPStrVerificationCode;
    }

    public String getERPStrVerificationCode() {
        return ERPStrVerificationCode;
    }
  
    // function to generate a random string of length n

    public void setErpRecoverUserPassword(String erpRecoverUserPassword) {
        this.erpRecoverUserPassword = erpRecoverUserPassword;
    }

    public String getErpRecoverUserPassword() {
        return erpRecoverUserPassword;
    }
    

}
