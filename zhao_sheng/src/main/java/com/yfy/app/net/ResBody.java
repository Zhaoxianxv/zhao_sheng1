package com.yfy.app.net;

import com.yfy.app.net.affiche.SChoolGetMenuRes;
import com.yfy.app.net.affiche.SchoolGetBannerRes;
import com.yfy.app.net.affiche.SchoolGetNewsListRes;
import com.yfy.app.net.applied_order.BOrderMainRes;
import com.yfy.app.net.applied_order.OrderAdminSetRes;
import com.yfy.app.net.applied_order.OrderApplyNewRes;
import com.yfy.app.net.applied_order.OrderGetAdminListRes;
import com.yfy.app.net.applied_order.OrderGetCountRes;
import com.yfy.app.net.applied_order.OrderGetApplyGradeRes;
import com.yfy.app.net.applied_order.OrderGetDetailRes;
import com.yfy.app.net.applied_order.OrderGetLogisticsListRes;
import com.yfy.app.net.applied_order.OrderGetRoomNameRes;
import com.yfy.app.net.applied_order.OrderGetUserListRes;
import com.yfy.app.net.applied_order.OrderQueryRoomDayRes;
import com.yfy.app.net.applied_order.OrderSetScoreRes;
import com.yfy.app.net.atten.AttenAdminDoingRes;
import com.yfy.app.net.atten.AttenGetAdminListRes;
import com.yfy.app.net.atten.AttenApplyRes;
import com.yfy.app.net.atten.AttenDelItemRes;
import com.yfy.app.net.atten.AttenGetAuditorListRes;
import com.yfy.app.net.atten.AttenGetCountRes;
import com.yfy.app.net.atten.AttenGetTypeRes;
import com.yfy.app.net.atten.AttenGetUserListRes;
import com.yfy.app.net.atten.BAttenMainRes;
import com.yfy.app.net.authentication.AuthenticationGetDataRes;
import com.yfy.app.net.authentication.AuthenticationLoginRes;
import com.yfy.app.net.base.BaseGetTokenRes;
import com.yfy.app.net.base.UserChangePasswordRes;
import com.yfy.app.net.base.UserGetBaseInfoRes;
import com.yfy.app.net.base.UserResetCallRes;
import com.yfy.app.net.base.UserResetPasswordRes;
import com.yfy.app.net.base.UserSignRes;
import com.yfy.app.net.check.CheckAddChildRes;
import com.yfy.app.net.check.CheckAddParentRes;
import com.yfy.app.net.check.CheckGetClassRes;
import com.yfy.app.net.check.CheckGetIllTypeRes;
import com.yfy.app.net.check.CheckGetStuToIllRes;
import com.yfy.app.net.check.CheckStuDelChildRes;
import com.yfy.app.net.check.CheckStuDelParentRes;
import com.yfy.app.net.check.CheckStuParentDetailRes;
import com.yfy.app.net.check.CheckGetStuRes;
import com.yfy.app.net.check.CheckGetTypeRes;
import com.yfy.app.net.check.CheckStuChildDetailRes;
import com.yfy.app.net.check.CheckSubimtYesRes;
import com.yfy.app.net.check.CheckTjListRes;
import com.yfy.app.net.delay_service.DelayAdminClassSetRes;
import com.yfy.app.net.delay_service.DelayCopySetRes;
import com.yfy.app.net.delay_service.DelayDelStuItemRes;
import com.yfy.app.net.delay_service.DelayAdminGetTodayListRes;
import com.yfy.app.net.delay_service.DelayAdminGetToClassEventDetailRes;
import com.yfy.app.net.delay_service.DelayAdminGetClassListRes;
import com.yfy.app.net.delay_service.DelayGetClassListReplaceRes;
import com.yfy.app.net.delay_service.DelayGetClassListTeaRes;
import com.yfy.app.net.delay_service.DelayGetClassStuListRes;
import com.yfy.app.net.delay_service.DelayGetCopyListRes;
import com.yfy.app.net.delay_service.DelayGetItemDetailRes;
import com.yfy.app.net.delay_service.DelayGetTypeRes;
import com.yfy.app.net.delay_service.DelayTeaAddRes;
import com.yfy.app.net.duty.DutyGetReportDetailRes;
import com.yfy.app.net.duty.DutyPlaneRes;
import com.yfy.app.net.duty.WeekAllRes;
import com.yfy.app.net.base.UserGetAdminRes;
import com.yfy.app.net.ebook.BookGetTypeRes;
import com.yfy.app.net.base.UserGetCallRes;
import com.yfy.app.net.duty.DutyTypeRes;
import com.yfy.app.net.ebook.BookGetUserListRes;
import com.yfy.app.net.ebook.VideoGetUserListRes;
import com.yfy.app.net.event.EventAddRes;
import com.yfy.app.net.event.EventDelImageRes;
import com.yfy.app.net.event.EventDelRes;
import com.yfy.app.net.event.EventGetMainListRes;
import com.yfy.app.net.event.EventGetDepRes;
import com.yfy.app.net.base.UserGetCurrentTermRes;
import com.yfy.app.net.event.EventGetMyListRes;
import com.yfy.app.net.event.EventGetWeekListRes;
import com.yfy.app.net.event.EventGetWeekRes;
import com.yfy.app.net.footbook.FootBookAddSuggestRes;
import com.yfy.app.net.footbook.FootBookGetPopularRes;
import com.yfy.app.net.footbook.FootBookGetRes;
import com.yfy.app.net.footbook.FootBookPraiseRes;
import com.yfy.app.net.goods.GoodAddApplyRes;
import com.yfy.app.net.goods.GoodAddStationeryTypeRes;
import com.yfy.app.net.goods.GoodAdminApplyRes;
import com.yfy.app.net.goods.GoodDelUserItemRes;
import com.yfy.app.net.goods.GoodGetItemDetailRes;
import com.yfy.app.net.goods.GoodGetRecordListAdminRes;
import com.yfy.app.net.goods.GoodGetRecordListMasterRes;
import com.yfy.app.net.goods.GoodGetRecordListUserRes;
import com.yfy.app.net.goods.GoodSearchGetStationeryTypeRes;
import com.yfy.app.net.login.UserDuplicationLoginRes;
import com.yfy.app.net.login.UserGetDuplicationListRes;
import com.yfy.app.net.login.UserLoginRes;
import com.yfy.app.net.base.NticeNumRes;
import com.yfy.app.net.login.PhoneCodeRes;
import com.yfy.app.net.base.ReadNoticeRes;
import com.yfy.app.net.login.ResetPasswordGetCodeRes;
import com.yfy.app.net.duty.DutyUserListRes;
import com.yfy.app.net.goods.GoodNumAdminListRes;
import com.yfy.app.net.goods.GoodNumCountRes;
import com.yfy.app.net.goods.GoodNumAddRes;
import com.yfy.app.net.goods.GoodNumDoRes;
import com.yfy.app.net.goods.GoodNumGetByIdRes;
import com.yfy.app.net.goods.GoodNumUserListRes;
import com.yfy.app.net.goods.GoodGetCountAdminRes;
import com.yfy.app.net.goods.GoodGetCountSchoolRes;
import com.yfy.app.net.goods.GoodGetRecordListSchoolRes;
import com.yfy.app.net.goods.GoodGetCountMasterRes;
import com.yfy.app.net.goods.GoodGetMasterUserRes;
import com.yfy.app.net.goods.GoodGetStationeryTypeRes;
import com.yfy.app.net.login.UserLogoutRes;
import com.yfy.app.net.login.UserResetPicRes;
import com.yfy.app.net.maintain.BMaintainDetailRes;
import com.yfy.app.net.maintain.BMaintainMainRes;
import com.yfy.app.net.maintain.MaintainAdminSetStateRes;
import com.yfy.app.net.maintain.MaintainApplyRes;
import com.yfy.app.net.maintain.MaintainGetAdminListRes;
import com.yfy.app.net.maintain.MaintainDelItemRes;
import com.yfy.app.net.maintain.MaintainSetSectionRes;
import com.yfy.app.net.notice.NoticeAddRes;
import com.yfy.app.net.notice.NoticeGetSendBoxListRes;
import com.yfy.app.net.notice.NoticeGetUserListRes;
import com.yfy.app.net.process.ProcessAdminGetListRes;
import com.yfy.app.net.process.ProcessAdminSetClassRes;
import com.yfy.app.net.process.ProcessAdminSetStateRes;
import com.yfy.app.net.process.ProcessApplyDetailRes;
import com.yfy.app.net.process.ProcessDelDetailRes;
import com.yfy.app.net.process.ProcessGetDetailRes;
import com.yfy.app.net.process.ProcessGetGroupRes;
import com.yfy.app.net.process.ProcessGetTermRes;
import com.yfy.app.net.process.ProcessGetTypeRes;
import com.yfy.app.net.process.ProcessGetUserCountRes;
import com.yfy.app.net.process.ProcessGetUserListRes;
import com.yfy.app.net.satisfaction.SatisfactionStuSetSchoolScoreRes;
import com.yfy.app.net.satisfaction.SatisfactionStuSetTeaScoreRes;
import com.yfy.app.net.seal.SealAdminListRes;
import com.yfy.app.net.seal.SealAllDetailRes;
import com.yfy.app.net.seal.SealApplyAddRes;
import com.yfy.app.net.seal.SealApplyMasterUserRes;
import com.yfy.app.net.seal.SealByIdItemRes;
import com.yfy.app.net.seal.SealGetSealTypeRes;
import com.yfy.app.net.seal.SealMasterCountRes;
import com.yfy.app.net.seal.SealSetDoRes;
import com.yfy.app.net.seal.SealUserListRes;
import com.yfy.app.net.judge.TeaJudgeAddRes;
import com.yfy.app.net.judge.TeaJudgeDelImageRes;
import com.yfy.app.net.judge.TeaJudgeDelItemRes;
import com.yfy.app.net.judge.TeaJudgeGetInfoYearListRes;
import com.yfy.app.net.satisfaction.SatisfactionGetDetailRes;
import com.yfy.app.net.satisfaction.SatisfactionStuGetTeaRes;
import com.yfy.app.net.satisfaction.SatisfactionStuGetSchoolRes;
import com.yfy.app.net.satisfaction.SatisfactionTeaGetStuRes;
import com.yfy.app.net.satisfaction.SatisfactionTeaGetMainRes;
import com.yfy.app.net.base.UserGetTermListRes;
import com.yfy.app.net.ebook.VideoGetTypeRes;
import com.yfy.app.net.maintain.MaintainGetSectionRes;
import com.yfy.app.net.maintain.MaintainGetCountRes;
import com.yfy.app.net.maintain.MaintainGetUserListRes;
import com.yfy.app.net.notice.NoticeGetUserDetailRes;
import com.yfy.app.net.notice.NoticeGetStuListRes;
import com.yfy.app.net.notice.NoticeGetTeaListRes;
import com.yfy.app.net.notice.NoticeReadRes;
import com.yfy.app.net.notice.NoticeGetSendBoxDetailRes;
import com.yfy.app.net.judge.TeaJudgeGetAddTypeRes;
import com.yfy.app.net.judge.TeaJudgeGetTjDataRes;
import com.yfy.app.net.judge.TeaJudgeGetInfoDetailRes;
import com.yfy.app.net.judge.TeaJudgeGetTjTypeRes;
import com.yfy.app.net.judge.TeaJudgeGetAddParameterRes;
import com.yfy.app.net.judge.TeaJudgeGetYearRes;
import com.yfy.app.net.vote.VoteGetItemDetailRes;
import com.yfy.app.net.vote.VoteGetMainListRes;
import com.yfy.app.net.vote.VoteSubmitRes;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 用户角色返回body
 * Created by SmileXie on 16/7/15.
 */
@Root(name = TagFinal.BODY)
public class ResBody {

    @Element(name = TagFinal.BASE_SAVE_IMG +Base.RESPONSE, required = false)
    public SaveImgRes saveImgRes;

    @Element(name = TagFinal.BASE_GET_TOKEN +Base.RESPONSE, required = false)
    public BaseGetTokenRes baseGetTookenRes;

    @Element(name = TagFinal.USER_GET_CURRENT_TERM +Base.RESPONSE, required = false)
    public UserGetCurrentTermRes userGetCurrentTermRes;

    @Element(name = TagFinal.GETNOTICENUM+Base.RESPONSE, required = false)
    public NticeNumRes getnoticenumResponse;

    @Element(name = TagFinal.READNOTICE+Base.RESPONSE, required = false)
    public ReadNoticeRes readnoticeResponse;

    @Element(name = TagFinal.USER_GET_CODE_TO_SYSTEM_PHONE +Base.RESPONSE, required = false)
    public PhoneCodeRes phoneCodeRes;

    @Element(name = TagFinal.USER_GET_CODE_TO_EDIT_PHONE +Base.RESPONSE, required = false)
    public ResetPasswordGetCodeRes resetPasswordGetCodeRes;

    @Element(name = TagFinal.USER_INFO+Base.RESPONSE, required = false)
    public UserGetBaseInfoRes userGetBaseInfoRes;

    //---------------------新生报名-------------


    @Element(name = TagFinal.AUTHENTICATION_GET_STU +"Response", required = false)
    public AuthenticationGetDataRes authenticationGetDataRes;

    @Element(name = TagFinal.AUTHENTICATION_LOGIN +"Response", required = false)
    public AuthenticationLoginRes authenticationLoginRes;
    /**
     * --------------------user-------------------------
     */
    @Element(name = TagFinal.USER_ADD_HEAD+"Response", required = false)
    public UserResetPicRes userResetPicRes;

    @Element(name = TagFinal.USER_GET_TERM_LIST +"Response", required = false)
    public UserGetTermListRes userGetTermListRes;

    @Element(name = TagFinal.USER_GET_MOBILE+"Response", required = false)
    public UserGetCallRes userGetCallRes;

    @Element(name = TagFinal.USER_SET_MOBILE+"Response", required = false)
    public UserResetCallRes userResetCallRes;

    @Element(name = TagFinal.USER_LOGIN +"Response", required = false)
    public UserLoginRes userLoginRes;

    @Element(name = TagFinal.USER_GET_DUPLICATION_LIST +Base.RESPONSE, required = false)
    public UserGetDuplicationListRes userGetDuplicationListRes;

    @Element(name = TagFinal.USER_DUPLICATION_LOGIN +Base.RESPONSE, required = false)
    public UserDuplicationLoginRes userDuplicationLoginRes;

    @Element(name = TagFinal.USER_SIGN +Base.RESPONSE, required = false)
    public UserSignRes userSignRes;

    @Element(name = TagFinal.USER_LOGOUT +Base.RESPONSE, required = false)
    public UserLogoutRes userLogoutRes;

    @Element(name = TagFinal.USER_GET_ADMIN +Base.RESPONSE, required = false)
    public UserGetAdminRes userGetAdminRes;

    @Element(name = TagFinal.USER_CHANGE_PASSWORD +Base.RESPONSE, required = false)
    public UserChangePasswordRes userChangePasswordRes;

    @Element(name = TagFinal.USER_RESET_PASSWORD +Base.RESPONSE, required = false)
    public UserResetPasswordRes userResetPasswordRes;
    /**
     * -----------------affiche  school news  vote---------------
     */
    @Element(name = TagFinal.SCHOOL_GET_NEWS_LIST +"Response", required = false)
    public SchoolGetNewsListRes schoolGetNewsListRes;


    @Element(name = TagFinal.SCHOOL_GET_MENU+Base.RESPONSE, required = false)
    public SChoolGetMenuRes sChoolGetMenuRes;
    @Element(name = TagFinal.SCHOOL_GET_NEWS_BANNER +"Response", required = false)
    public SchoolGetBannerRes schoolBannerRes;

    @Element(name = TagFinal.VOTE_GET_MAIN_LIST+"Response", required = false)
    public VoteGetMainListRes voteGetMainListRes;
    @Element(name = TagFinal.VOTE_GET_ITEM_DETAIL+"Response", required = false)
    public VoteGetItemDetailRes voteGetItemDetailRes;
    @Element(name = TagFinal.VOTE_SUBMIT+"Response", required = false)
    public VoteSubmitRes voteSubmitRes;

    /**
     * -----------------foot book---------------------
     */

    @Element(name = TagFinal.FOOT_BOOK_GET_WEEK +"Response", required = false)
    public FootBookGetRes footBookGetRes;
    @Element(name = TagFinal.FOOT_BOOK_PRAISE +"Response", required = false)
    public FootBookPraiseRes footBookPraiseRes;
    @Element(name = TagFinal.FOOT_BOOK_GET_POPULAR +"Response", required = false)
    public FootBookGetPopularRes footBookGetPopularRes;
    @Element(name = TagFinal.FOOT_BOOK_ADD_SUGGEST +"Response", required = false)
    public FootBookAddSuggestRes footBookAddSuggestRes;
    /**
     * -----------------book---------------------
     */

    @Element(name = TagFinal.VIDEO_GET_TAG+"Response", required = false)
    public VideoGetTypeRes videoGetTypeRes;
    @Element(name = TagFinal.VIDEO_GET_MAIN_LIST+"Response", required = false)
    public VideoGetUserListRes videoGetUserListRes;

    @Element(name = TagFinal.BOOK_GET_TAG+"Response", required = false)
    public BookGetTypeRes bookGetTypeRes;

    @Element(name = TagFinal.BOOK_GET_USER_LIST+"Response", required = false)
    public BookGetUserListRes bookGetUserListRes;

    /**
     * ---------------------event 大事记---------------------
     */

    @Element(name = TagFinal.EVENT_GET_SECTION +Base.RESPONSE, required = false)
    public EventGetDepRes eventGetDepRes;

    @Element(name = TagFinal.EVENT_GET_USER_LIST +Base.RESPONSE, required = false)
    public EventGetMyListRes eventGetMyListRes;

    @Element(name = TagFinal.EVENT_GET_DATE_LIST+Base.RESPONSE, required = false)
    public EventGetMainListRes eventGetMainListRes;

    @Element(name = TagFinal.EVENT_GET_WEEK+Base.RESPONSE, required = false)
    public EventGetWeekRes eventGetWeekRes;

    @Element(name = TagFinal.EVENT_GET_WEEK_LIST+Base.RESPONSE, required = false)
    public EventGetWeekListRes eventGetWeekListRes;

    @Element(name = TagFinal.EVENT_DEL+Base.RESPONSE, required = false)
    public EventDelRes eventDelRes;

    @Element(name = TagFinal.EVENT_DEL_IMAGE+Base.RESPONSE, required = false)
    public EventDelImageRes eventDelImageRes;

    @Element(name = TagFinal.EVENT_ADD+Base.RESPONSE, required = false)
    public EventAddRes eventAddRes;
    //--------------------duty-----------

    @Element(name = TagFinal.DUTY_GET_USER+"Response", required = false)
    public DutyUserListRes dutyUserListRes;

    @Element(name = TagFinal.DUTY_TYPE+"Response", required = false)
    public DutyTypeRes duty_type_sponse;


    @Element(name = TagFinal.USER_GET_WEEK_ALL+"Response", required = false)
    public WeekAllRes week_all_sponse;


    @Element(name = TagFinal.DUTY_GET_PLANE+"Response", required = false)
    public DutyPlaneRes dutyPlaneRes;

    @Element(name = TagFinal.DUTY_GET_ADD_DETAILS+"Response", required = false)
    public DutyGetReportDetailRes dutyGetReportDetailRes;


    //--------------------notice-------

    @Element(name = TagFinal.NOTICE_GET_SEND_BOX_DETAIL +"Response", required = false)
    public NoticeGetSendBoxDetailRes noticeGetSendBoxDetailRes;

    @Element(name = TagFinal.NOTICE_GET_USER_DETAIL +"Response", required = false)
    public NoticeGetUserDetailRes noticeGetUserDetailRes;

    @Element(name = TagFinal.NOTICE_GET_USER_LIST +"Response", required = false)
    public NoticeGetUserListRes noticeGetUserListRes;

    @Element(name = TagFinal.NOTICE_GET_SEND_BOX_LIST +"Response", required = false)
    public NoticeGetSendBoxListRes noticeGetSendBoxListRes;

    @Element(name = TagFinal.NOTICE_GET_TEA+"Response", required = false)
    public NoticeGetTeaListRes noticeGetTeaListRes;

    @Element(name = TagFinal.NOTICE_GET_STU+"Response", required = false)
    public NoticeGetStuListRes noticeGetStuListRes;

    @Element(name = TagFinal.NOTICE_READ+"Response", required = false)
    public NoticeReadRes noticeReadRes;

    @Element(name = TagFinal.NOTICE_SEND+"Response", required = false)
    public NoticeAddRes noticeAddRes;

//    @Element(name = TagFinal.NOTICE_GET_TEA_CONTACTS+"Response", required = false)
//    public NoticeGetTeaConListRes noticeGetTeaConListRes;

    //------------------maintain----------------------

    @Element(name = TagFinal.MAINTENANCE_DELETE_MAINTAIN +Base.RESPONSE, required = false)
    public MaintainDelItemRes maintainDelItemRes;

    @Element(name = TagFinal.MAINTENANCE_GET_MAIN_LIST_USER +"Response", required = false)
    public MaintainGetUserListRes maintainGetUserListRes;

    @Element(name = TagFinal.MAINTENANCE_GET_MAIN_LIST_ADMIN +Base.RESPONSE, required = false)
    public MaintainGetAdminListRes maintainGetAdminListRes;

    @Element(name = TagFinal.MAINTENANCE_GET_COUNT +"Response", required = false)
    public MaintainGetCountRes maintainGetCountRes;

    @Element(name =TagFinal.MAINTENANCE_GET_SECTION +"Response", required = false)
    public MaintainGetSectionRes maintainGetSectionRes;

    @Element(name = TagFinal.MAINTENANCE_SET_SECTION +Base.RESPONSE, required = false)
    public MaintainSetSectionRes maintainSetSectionRes;

    @Element(name = TagFinal.MAINTENANCE_ADD +Base.RESPONSE, required = false)
    public MaintainApplyRes maintainApplyRes;

    @Element(name = TagFinal.MAINTENANCE_ADMIN_SET_STATE +Base.RESPONSE, required = false)
    public MaintainAdminSetStateRes maintainAdminSetStateRes;

    @Element(name = TagFinal.BOSS_MAINTAIN_LIST_DETAIL+Base.RESPONSE, required = false)
    public BMaintainDetailRes bMaintainDetailRes;

    @Element(name = TagFinal.BOSS_MAINTAIN_LIST+Base.RESPONSE, required = false)
    public BMaintainMainRes bMaintainMainRes;

//----------------------atten-----------------

    @Element(name = TagFinal.ATTENDANCE_APPLY +Base.RESPONSE, required = false)
    public AttenApplyRes attenApplyRes;

    @Element(name = TagFinal.ATTENDANCE_DELETE +Base.RESPONSE, required = false)
    public AttenDelItemRes attenDelItemRes;

    @Element(name = TagFinal.ATTENDANCE_GET_COUNT +"Response", required = false)
    public AttenGetCountRes attenGetCountRes;

    @Element(name = TagFinal.ATTENDANCE_GET_TYPE +"Response", required = false)
    public AttenGetTypeRes attenGetTypeRes;

    @Element(name = TagFinal.ATTENDANCE_GET_AUDITOR_LIST +"Response", required = false)
    public AttenGetAuditorListRes attenGetAuditorListRes;

    @Element(name = TagFinal.ATTENDANCE_GET_USER_LIST +"Response", required = false)
    public AttenGetUserListRes attenGetUserListRes;

    @Element(name = TagFinal.ATTENDANCE_GET_ADMIN_LIST +"Response", required = false)
    public AttenGetAdminListRes attenGetAdminListRes;

    @Element(name = TagFinal.ATTENDANCE_ADMIN_DO + Base.RESPONSE, required = false)
    public AttenAdminDoingRes attenAdminDoingRes;

    @Element(name = TagFinal.BOSS_ATTEN_LIST + Base.RESPONSE, required = false)
    public BAttenMainRes bAttenMainRes;
    //----------------applied  order---------------

    @Element(name = TagFinal.ORDER_GET_COUNT+Base.RESPONSE, required = false)
    public OrderGetCountRes orderGetCountRes;

    @Element(name = TagFinal.ORDER_GET_USER_LIST+Base.RESPONSE, required = false)
    public OrderGetUserListRes orderGetUserListRes;

    @Element(name = TagFinal.ORDER_GET_ADMIN_LIST+Base.RESPONSE, required = false)
    public OrderGetAdminListRes orderGetAdminListRes;

    @Element(name = TagFinal.ORDER_GET_LOGISTICS_LIST+Base.RESPONSE, required = false)
    public OrderGetLogisticsListRes orderGetLogisticsListRes;

    @Element(name = TagFinal.ORDER_GET_DETAIL+Base.RESPONSE, required = false)
    public OrderGetDetailRes orderGetDetailRes;

    @Element(name = TagFinal.ORDER_ADD+Base.RESPONSE, required = false)
    public OrderApplyNewRes orderApplyNewRes;

    @Element(name = TagFinal.ORDER_GET_ROOM_NAME+Base.RESPONSE, required = false)
    public OrderGetRoomNameRes orderGetRoomNameRes;
    @Element(name = TagFinal.ORDER_GET_APPLY_GRADE+Base.RESPONSE, required = false)
    public OrderGetApplyGradeRes orderGetApplyGradeRes;

    @Element(name = TagFinal.ORDER_QUERY+Base.RESPONSE, required = false)
    public OrderQueryRoomDayRes orderQueryRoomDayRes;

    @Element(name = TagFinal.ORDER_ADMIN_SET+Base.RESPONSE, required = false)
    public OrderAdminSetRes orderAdminSetRes;

    @Element(name = TagFinal.ORDER_SET_SCORE+Base.RESPONSE, required = false)
    public OrderSetScoreRes orderSetScoreRes;

    @Element(name = TagFinal.BOSS_ORDER_LIST+"Response", required = false)
    public BOrderMainRes bOrderMainRes;


    //----------------tea evaluate----------------------

    @Element(name = TagFinal.TEA_JUDGE_GET_ADD_TYPE +"Response", required = false)
    public TeaJudgeGetAddTypeRes teaJudgeGetAddTypeRes;

    @Element(name =  TagFinal.TEA_JUDGE_GET_STATISTICS_TYPE +"Response", required = false)
    public TeaJudgeGetTjTypeRes teaJudgeGetTjTypeRes;

    @Element(name =  TagFinal.TEA_JUDGE_GET_STATISTICS_DATA +"Response", required = false)
    public TeaJudgeGetTjDataRes teaJudgeGetTjDataRes;

    @Element(name = TagFinal.TEA_JUDGE_GET_YEAR +"Response", required = false)
    public TeaJudgeGetYearRes teaJudgeGetYearRes;

    @Element(name =  TagFinal.TEA_JUDGE_GET_ADD_PARAMETER +"Response", required = false)
    public TeaJudgeGetAddParameterRes teaJudgeGetAddParameterRes;

    @Element(name = TagFinal.TEA_JUDGE_GET_INFO_DETAIL +"Response", required = false)
    public TeaJudgeGetInfoDetailRes teaJudgeGetInfoDetailRes;

    @Element(name = TagFinal.TEA_JUDGE_DEL_ITEM +"Response", required = false)
    public TeaJudgeDelItemRes teaJudgeDelItemRes;

    @Element(name = TagFinal.TEA_JUDGE_DEL_PIC +"Response", required = false)
    public TeaJudgeDelImageRes teaJudgeDelImageRes;

    @Element(name = TagFinal.TEA_JUDGE_ADD +"Response", required = false)
    public TeaJudgeAddRes teaJudgeAddRes;

    @Element(name = TagFinal.TEA_JUDGE_GET_INFO_YEAR_LIST +"Response", required = false)
    public TeaJudgeGetInfoYearListRes teaJudgeGetInfoYearListRes;


    //------------------satisfaction-------------------

    @Element(name = TagFinal.SATISFACTION_STU_GET_TEAS +"Response", required = false)
    public SatisfactionStuGetTeaRes satisfactionStuGetTeaRes;

    @Element(name = TagFinal.SATISFACTION_STU_GET_SCHOOL +"Response", required = false)
    public SatisfactionStuGetSchoolRes satisfactionStuGetSchoolRes;

    @Element(name = TagFinal.SATISFACTION_STU_SET_SCHOOL_SCORE +"Response", required = false)
    public SatisfactionStuSetSchoolScoreRes satisfactionStuSetSchoolScoreRes;

    @Element(name = TagFinal.SATISFACTION_STU_SET_TEA_SCORE +"Response", required = false)
    public SatisfactionStuSetTeaScoreRes satisfactionStuSetTeaScoreRes;

    @Element(name = TagFinal.SATISFACTION_GET_DETAILS +"Response", required = false)
    public SatisfactionGetDetailRes satisfactionGetDetailRes;

    @Element(name = TagFinal.SATISFACTION_TEA_GET_MAIN +"Response", required = false)
    public SatisfactionTeaGetMainRes satisfactionTeaGetMainRes;

    @Element(name = TagFinal.SATISFACTION_TEA_GET_STU +"Response", required = false)
    public SatisfactionTeaGetStuRes satisfactionTeaGetStuRes;


    //---------------------goods-------------------

    @Element(name = TagFinal.GOODS_GET_COUNT_ADMIN +"Response", required = false)
    public GoodGetCountAdminRes goodGetCountAdminRes;

    @Element(name = TagFinal.GOODS_GET_COUNT_MASTER +"Response", required = false)
    public GoodGetCountMasterRes goodGetCountMasterRes;

    @Element(name = TagFinal.GOODS_GET_COUNT_SCHOOL +Base.RESPONSE,required = false)
    public GoodGetCountSchoolRes goodGetCountSchoolRes;

    @Element(name = TagFinal.GOODS_GET_USER_RECORD_LIST +Base.RESPONSE,required = false)
    public GoodGetRecordListUserRes goodGetRecordListUserRes;

    @Element(name = TagFinal.GOODS_GET_ADMIN_RECORD_LIST +Base.RESPONSE,required = false)
    public GoodGetRecordListAdminRes goodGetRecordListAdminRes;

    @Element(name = TagFinal.GOODS_GET_MASTER_RECORD_LIST +Base.RESPONSE,required = false)
    public GoodGetRecordListMasterRes goodGetRecordListMasterRes;

    @Element(name = TagFinal.GOODS_GET_SCHOOL_RECORD_LIST +Base.RESPONSE,required = false)
    public GoodGetRecordListSchoolRes goodGetRecordListSchoolRes;

    @Element(name = TagFinal.GOODS_GET_ITEM_DETAILS +Base.RESPONSE,required = false)
    public GoodGetItemDetailRes goodGetItemDetailRes;

    @Element(name = TagFinal.GOODS_GET_STATIONERY_TYPE +"Response", required = false)
    public GoodGetStationeryTypeRes goodGetStationeryTypeRes;

    @Element(name = TagFinal.GOODS_GET_MASTER_USER +"Response", required = false)
    public GoodGetMasterUserRes goodGetMasterUserRes;

    @Element(name = TagFinal.GOODS_DELETE_ITEM +"Response", required = false)
    public GoodDelUserItemRes goodDelUserItemRes;

    @Element(name = TagFinal.GOODS_SEARCH_GET_STATIONERY_TYPE +"Response", required = false)
    public GoodSearchGetStationeryTypeRes goodSearchGetStationeryTypeRes;

    @Element(name = TagFinal.GOODS_ADD_APPLY +"Response", required = false)
    public GoodAddApplyRes goodAddApplyRes;

    @Element(name = TagFinal.GOODS_ADD_STATIONERY_TYPE +"Response", required = false)
    public GoodAddStationeryTypeRes goodAddStationeryTypeRes;

    @Element(name = TagFinal.GOODS_ADMIN_APPLY +"Response", required = false)
    public GoodAdminApplyRes goodAdminApplyRes;

    @Element(name = TagFinal.GOODS_NUM_GET_COUNT +"Response", required = false)
    public GoodNumCountRes goodNumCountRes;

    @Element(name = TagFinal.GOODS_NUM_ADD+"Response", required = false)
    public GoodNumAddRes goodNumAddRes;

    @Element(name = TagFinal.GOODS_NUM_GET_USER_LIST +"Response", required = false)
    public GoodNumUserListRes goodNumUserListRes;

    @Element(name = TagFinal.GOODS_NUM_GET_ADMIN_LIST +"Response", required = false)
    public GoodNumAdminListRes goodNumAdminListRes;

    @Element(name = TagFinal.GOODS_NUM_GET_BYID+"Response", required = false)
    public GoodNumGetByIdRes goodNumGetByIdRes;

    @Element(name = TagFinal.GOODS_NUM_DO_STATE+"Response", required = false)
    public GoodNumDoRes goodNumDoRes;

    //----------------------------check------------------

    @Element(name = TagFinal.CHECK_GET_TYPE+Base.RESPONSE,required = false)
    public CheckGetTypeRes checkGetTypeRes;

    @Element(name = TagFinal.CHECK_GET_ILL_TYPE+Base.RESPONSE,required = false)
    public CheckGetIllTypeRes checkGetIllTypeRes;

    @Element(name = TagFinal.CHECK_GET_CLASS+Base.RESPONSE,required = false)
    public CheckGetClassRes checkGetClassRes;

    @Element(name = TagFinal.CHECK_GET_STU+Base.RESPONSE,required = false)
    public CheckGetStuRes checkGetStuRes;
    @Element(name = TagFinal.CHECK_GET_STU_TO_TYPE_ILL+Base.RESPONSE,required = false)
    public CheckGetStuToIllRes checkGetStuToIllRes;

    @Element(name = TagFinal.CHECK_GET_STU_ITEM_DETAIL +Base.RESPONSE,required = false)
    public CheckStuParentDetailRes checkStuParentDetailRes;

    @Element(name = TagFinal.CHECK_GET_STU_ALL_DETAIL +Base.RESPONSE,required = false)
    public CheckStuChildDetailRes checkStuChildDetailRes;

    @Element(name = TagFinal.CHECK_SUBMIT_YES+Base.RESPONSE,required = false)
    public CheckSubimtYesRes checkSubimtYesRes;

    @Element(name = TagFinal.CHECK_ADD_PARENT+Base.RESPONSE,required = false)
    public CheckAddParentRes checkAddParentRes;

    @Element(name = TagFinal.CHECK_ADD_CHILD+Base.RESPONSE,required = false)
    public CheckAddChildRes checkAddChildRes;

    @Element(name = TagFinal.CHECK_DEL_CHILD+Base.RESPONSE,required = false)
    public CheckStuDelChildRes checkStuDelChildRes;

    @Element(name = TagFinal.CHECK_DEL_PARENT+Base.RESPONSE,required = false)
    public CheckStuDelParentRes checkStuDelParentRes;

    @Element(name = TagFinal.CHECK_GET_TJ+Base.RESPONSE,required = false)
    public CheckTjListRes checkTjListRes;

    //------------------------seal-----------------
    @Element(name = TagFinal.SEAL_GET_SEAL_TYPE+Base.RESPONSE,required = false)
    public SealGetSealTypeRes sealGetSealTypeRes;


    @Element(name = TagFinal.SEAL_GET_ADMIN_LIST+Base.RESPONSE,required = false)
    public SealAdminListRes sealAdminListRes;


    @Element(name = TagFinal.SEAL_GET_USER_LIST+Base.RESPONSE,required = false)
    public SealUserListRes sealUserListRes;


    @Element(name = TagFinal.SEAL_GET_MASTER_COUNT+Base.RESPONSE,required = false)
    public SealMasterCountRes sealMasterCountRes;

    @Element(name = TagFinal.SEAL_GET_APPLY_MASTER_LIST+Base.RESPONSE,required = false)
    public SealApplyMasterUserRes sealApplyMasterUserRes;

    @Element(name = TagFinal.SEAL_SUBMIT+Base.RESPONSE,required = false)
    public SealApplyAddRes sealApplyAddRes;
    @Element(name = TagFinal.SEAL_GET_ITEM_BYID+Base.RESPONSE,required = false)
    public SealByIdItemRes sealByIdItemRes;

    @Element(name = TagFinal.SEAL_GET_ALL+Base.RESPONSE,required = false)
    public SealAllDetailRes sealAllDetailRes;

    @Element(name = TagFinal.SEAL_DO+Base.RESPONSE,required = false)
    public SealSetDoRes sealSetDoRes;

    //---------delay-----


    @Element(name = TagFinal.DELAY_ADMIN_GET_TODAY_LIST +Base.RESPONSE,required = false)
    public DelayAdminGetTodayListRes delayAdminGetTodayListRes;

    @Element(name = TagFinal.DELAY_ADMIN_get_TO_CLASS_EVENT_DETAIL +Base.RESPONSE,required = false)
    public DelayAdminGetToClassEventDetailRes delayAdminGetToClassEventDetailRes;

    @Element(name = TagFinal.DELAY_ADMIN_GET_CLASS_LIST +Base.RESPONSE,required = false)
    public DelayAdminGetClassListRes delayAdminGetClassListRes;

    @Element(name = TagFinal.DELAY_ADMIN_CLASS_SET +Base.RESPONSE,required = false)
    public DelayAdminClassSetRes delayAdminClassSetRes;
    @Element(name = TagFinal.DELAY_GET_TYPE+Base.RESPONSE,required = false)
    public DelayGetTypeRes delayGetTypeRes;
    @Element(name = TagFinal.DELAY_GET_ITEM_DETAIL+Base.RESPONSE,required = false)
    public DelayGetItemDetailRes delayGetItemDetailRes;
    @Element(name = TagFinal.DELAY_GET_CLASS_STU_LIST +Base.RESPONSE,required = false)
    public DelayGetClassStuListRes delayGetClassStuListRes;

    @Element(name = TagFinal.DELAY_DEL+Base.RESPONSE,required = false)
    public DelayDelStuItemRes delayDelStuItemRes;

    @Element(name = TagFinal.DELAY_GET_CLASS_LIST_TEA+Base.RESPONSE,required = false)
    public DelayGetClassListTeaRes delayGetClassListTeaRes;

    @Element(name = TagFinal.DELAY_GET_CLASS_LIST_REPLACE+Base.RESPONSE,required = false)
    public DelayGetClassListReplaceRes delayGetClassListReplaceRes;

    @Element(name = TagFinal.DELAY_GET_COPY_LIST+Base.RESPONSE,required = false)
    public DelayGetCopyListRes delayGetCopyListRes;

    @Element(name = TagFinal.DELAY_COPY_SET+Base.RESPONSE,required = false)
    public DelayCopySetRes delayCopySetRes;

    @Element(name = TagFinal.DELAY_TEA_ADD+Base.RESPONSE,required = false)
    public DelayTeaAddRes delayTeaAddRes;

    //-------------------process---------------------
    @Element(name = TagFinal.PROCESS_GET_TYPE+Base.RESPONSE,required = false)
    public ProcessGetTypeRes processGetTypeRes;

    @Element(name = TagFinal.PROCESS_GET_TERM+Base.RESPONSE,required = false)
    public ProcessGetTermRes processGetTermRes;

    @Element(name = TagFinal.PROCESS_GET_GROUP+Base.RESPONSE,required = false)
    public ProcessGetGroupRes processGetGroupRes;

    @Element(name = TagFinal.PROCESS_GET_USER_COUNT+Base.RESPONSE,required = false)
    public ProcessGetUserCountRes processGetUserCountRes;

    @Element(name = TagFinal.PROCESS_ADD+Base.RESPONSE,required = false)
    public ProcessApplyDetailRes processApplyDetailRes;

    @Element(name = TagFinal.PROCESS_GET_USER_DATA_LIST+Base.RESPONSE,required = false)
    public ProcessGetUserListRes processGetUserListRes;

    @Element(name = TagFinal.PROCESS_GET_ITEM_DETAIL+Base.RESPONSE,required = false)
    public ProcessGetDetailRes processGetDetailRes;

    @Element(name = TagFinal.PROCESS_DEL_ITEM_DETAIL+Base.RESPONSE,required = false)
    public ProcessDelDetailRes processDelDetailRes;

    @Element(name = TagFinal.PROCESS_ADMIN_GET_DATA_LIST+Base.RESPONSE,required = false)
    public ProcessAdminGetListRes processAdminGetListRes;

    @Element(name = TagFinal.PROCESS_ADMIN_SET_CLASS+Base.RESPONSE,required = false)
    public ProcessAdminSetClassRes processAdminSetClassRes;

    @Element(name = TagFinal.PROCESS_ADMIN_SET_STATE+Base.RESPONSE,required = false)
    public ProcessAdminSetStateRes processAdminSetStateRes;





}
