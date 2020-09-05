package com.yfy.app.net;

import com.yfy.app.net.affiche.SchoolGetBannerReq;
import com.yfy.app.net.affiche.SchoolGetMenuReq;
import com.yfy.app.net.affiche.SchoolGetNewsListReq;
import com.yfy.app.net.applied_order.BOrderMainReq;
import com.yfy.app.net.applied_order.OrderAdminSetReq;
import com.yfy.app.net.applied_order.OrderApplyNewReq;
import com.yfy.app.net.applied_order.OrderGetAdminListReq;
import com.yfy.app.net.applied_order.OrderGetCountReq;
import com.yfy.app.net.applied_order.OrderGetApplyGradeReq;
import com.yfy.app.net.applied_order.OrderGetDetailReq;
import com.yfy.app.net.applied_order.OrderGetLogisticsListReq;
import com.yfy.app.net.applied_order.OrderGetUserListReq;
import com.yfy.app.net.applied_order.OrderQueryRoomDayReq;
import com.yfy.app.net.applied_order.OrderGetRoomNameReq;
import com.yfy.app.net.applied_order.OrderSetScoreReq;
import com.yfy.app.net.atten.AttenAdminDoingReq;
import com.yfy.app.net.atten.AttenGetAdminListReq;
import com.yfy.app.net.atten.AttenApplyReq;
import com.yfy.app.net.atten.AttenDelItemReq;
import com.yfy.app.net.atten.AttenGetAuditorListReq;
import com.yfy.app.net.atten.AttenGetCountReq;
import com.yfy.app.net.atten.AttenGetTypeReq;
import com.yfy.app.net.atten.AttenGetUserListReq;
import com.yfy.app.net.atten.BAttenMainReq;
import com.yfy.app.net.authentication.AuthenticationGetDataReq;
import com.yfy.app.net.authentication.AuthenticationLoginReq;
import com.yfy.app.net.base.BaseGetTokenReq;
import com.yfy.app.net.base.UserChangePasswordReq;
import com.yfy.app.net.base.UserGetBaseInfoReq;
import com.yfy.app.net.base.UserResetCallReq;
import com.yfy.app.net.base.UserResetPasswordReq;
import com.yfy.app.net.base.UserSignReq;
import com.yfy.app.net.check.CheckAddChildReq;
import com.yfy.app.net.check.CheckAddParentReq;
import com.yfy.app.net.check.CheckGetClassReq;
import com.yfy.app.net.check.CheckGetIllTypeReq;
import com.yfy.app.net.check.CheckGetStuToIllReq;
import com.yfy.app.net.check.CheckStuDelChildReq;
import com.yfy.app.net.check.CheckStuDelParentReq;
import com.yfy.app.net.check.CheckStuParentDetailReq;
import com.yfy.app.net.check.CheckGetStuReq;
import com.yfy.app.net.check.CheckGetTypeReq;
import com.yfy.app.net.check.CheckStuChildDetailReq;
import com.yfy.app.net.check.CheckSubimtYesReq;
import com.yfy.app.net.check.CheckTjListReq;
import com.yfy.app.net.delay_service.DelayAdminClassSetReq;
import com.yfy.app.net.delay_service.DelayCopySetReq;
import com.yfy.app.net.delay_service.DelayDelStuItemReq;
import com.yfy.app.net.delay_service.DelayAdminGetTodayListReq;
import com.yfy.app.net.delay_service.DelayAdminGetToClassEventDetailReq;
import com.yfy.app.net.delay_service.DelayAdminGetClassListReq;
import com.yfy.app.net.delay_service.DelayGetClassListReplaceReq;
import com.yfy.app.net.delay_service.DelayGetClassListTeaReq;
import com.yfy.app.net.delay_service.DelayGetClassStuListReq;
import com.yfy.app.net.delay_service.DelayGetCopyListReq;
import com.yfy.app.net.delay_service.DelayGetItemDetailReq;
import com.yfy.app.net.delay_service.DelayGetTypeReq;
import com.yfy.app.net.delay_service.DelayTeaAddReq;
import com.yfy.app.net.duty.DutyGetReportDetailReq;
import com.yfy.app.net.duty.DutyPlaneReq;
import com.yfy.app.net.duty.WeekAllReq;
import com.yfy.app.net.base.UserGetAdminReq;
import com.yfy.app.net.ebook.BookGetTypeReq;
import com.yfy.app.net.base.UserGetCallReq;
import com.yfy.app.net.duty.DutyTypeReq;
import com.yfy.app.net.ebook.BookGetUserListReq;
import com.yfy.app.net.ebook.VideoGetUserListReq;
import com.yfy.app.net.event.EventAddReq;
import com.yfy.app.net.event.EventDelImageReq;
import com.yfy.app.net.event.EventDelReq;
import com.yfy.app.net.event.EventGetMainListReq;
import com.yfy.app.net.event.EventGetDepReq;
import com.yfy.app.net.base.UserGetCurrentTermReq;
import com.yfy.app.net.event.EventGetMyListReq;
import com.yfy.app.net.event.EventGetWeekListReq;
import com.yfy.app.net.event.EventGetWeekReq;
import com.yfy.app.net.footbook.FootBookAddSuggestReq;
import com.yfy.app.net.footbook.FootBookGetPopularReq;
import com.yfy.app.net.footbook.FootBookGetReq;
import com.yfy.app.net.footbook.FootBookPraiseReq;
import com.yfy.app.net.goods.GoodAddApplyReq;
import com.yfy.app.net.goods.GoodAddStationeryTypeReq;
import com.yfy.app.net.goods.GoodAdminApplyReq;
import com.yfy.app.net.goods.GoodDelUserItemReq;
import com.yfy.app.net.goods.GoodGetItemDetailReq;
import com.yfy.app.net.goods.GoodGetRecordListAdminReq;
import com.yfy.app.net.goods.GoodGetRecordListMasterReq;
import com.yfy.app.net.goods.GoodGetRecordListUserReq;
import com.yfy.app.net.goods.GoodSearchGetStationeryTypeReq;
import com.yfy.app.net.login.UserDuplicationLoginReq;
import com.yfy.app.net.login.UserGetDuplicationListReq;
import com.yfy.app.net.login.UserLoginReq;
import com.yfy.app.net.base.NticeNumReq;
import com.yfy.app.net.login.PhoneCodeReq;
import com.yfy.app.net.base.ReadNoticeReq;
import com.yfy.app.net.login.ResetPasswordGetCodeReq;
import com.yfy.app.net.duty.DutyUserListReq;
import com.yfy.app.net.goods.GoodNumAdminListReq;
import com.yfy.app.net.goods.GoodNumCountReq;
import com.yfy.app.net.goods.GoodNumAddReq;
import com.yfy.app.net.goods.GoodNumDoReq;
import com.yfy.app.net.goods.GoodNumGetByIdReq;
import com.yfy.app.net.goods.GoodNumUserListReq;
import com.yfy.app.net.goods.GoodGetCountAdminReq;
import com.yfy.app.net.goods.GoodGetCountSchoolReq;
import com.yfy.app.net.goods.GoodGetRecordListSchoolReq;
import com.yfy.app.net.goods.GoodGetCountMasterReq;
import com.yfy.app.net.goods.GoodGetMasterUserReq;
import com.yfy.app.net.goods.GoodGetStationeryTypeReq;
import com.yfy.app.net.login.UserLogoutReq;
import com.yfy.app.net.login.UserResetPicReq;
import com.yfy.app.net.maintain.BMaintainDetailReq;
import com.yfy.app.net.maintain.BMaintainMainReq;
import com.yfy.app.net.maintain.MaintainAdminSetStateReq;
import com.yfy.app.net.maintain.MaintainApplyReq;
import com.yfy.app.net.maintain.MaintainGetAdminListReq;
import com.yfy.app.net.maintain.MaintainDelItemReq;
import com.yfy.app.net.maintain.MaintainSetSectionReq;
import com.yfy.app.net.notice.NoticeAddReq;
import com.yfy.app.net.notice.NoticeGetSendBoxListReq;
import com.yfy.app.net.notice.NoticeGetUserListReq;
import com.yfy.app.net.process.ProcessAdminGetListReq;
import com.yfy.app.net.process.ProcessAdminSetClassReq;
import com.yfy.app.net.process.ProcessAdminSetStateReq;
import com.yfy.app.net.process.ProcessApplyDetailReq;
import com.yfy.app.net.process.ProcessDelDetailReq;
import com.yfy.app.net.process.ProcessGetDetailReq;
import com.yfy.app.net.process.ProcessGetGroupReq;
import com.yfy.app.net.process.ProcessGetTermReq;
import com.yfy.app.net.process.ProcessGetTypeReq;
import com.yfy.app.net.process.ProcessGetUserCountReq;
import com.yfy.app.net.process.ProcessGetUserListReq;
import com.yfy.app.net.satisfaction.SatisfactionStuSetSchoolScoreReq;
import com.yfy.app.net.satisfaction.SatisfactionStuSetTeaScoreReq;
import com.yfy.app.net.seal.SealAdminListReq;
import com.yfy.app.net.seal.SealAllDetailReq;
import com.yfy.app.net.seal.SealApplyAddReq;
import com.yfy.app.net.seal.SealApplyMasterUserReq;
import com.yfy.app.net.seal.SealByIdItemReq;
import com.yfy.app.net.seal.SealGetSealTypeReq;
import com.yfy.app.net.seal.SealMasterCountReq;
import com.yfy.app.net.seal.SealSetDoReq;
import com.yfy.app.net.seal.SealUserListReq;
import com.yfy.app.net.judge.TeaJudgeAddReq;
import com.yfy.app.net.judge.TeaJudgeDelImageReq;
import com.yfy.app.net.judge.TeaJudgeDelItemReq;
import com.yfy.app.net.judge.TeaJudgeGetInfoYearListReq;
import com.yfy.app.net.satisfaction.SatisfactionGetDetailReq;
import com.yfy.app.net.satisfaction.SatisfactionStuGetTeaReq;
import com.yfy.app.net.satisfaction.SatisfactionStuGetSchoolReq;
import com.yfy.app.net.satisfaction.SatisfactionTeaGetStuReq;
import com.yfy.app.net.satisfaction.SatisfactionTeaGetMainReq;
import com.yfy.app.net.base.UserGetTermListReq;
import com.yfy.app.net.ebook.VideoGetTypeReq;
import com.yfy.app.net.maintain.MaintainGetSectionReq;
import com.yfy.app.net.maintain.MaintainGetCountReq;
import com.yfy.app.net.maintain.MaintainGetUserListReq;
import com.yfy.app.net.notice.NoticeGetUserDetailReq;
import com.yfy.app.net.notice.NoticeGetStuListReq;
import com.yfy.app.net.notice.NoticeGetTeaListReq;
import com.yfy.app.net.notice.NoticeReadReq;
import com.yfy.app.net.notice.NoticeGetSendBoxDetailReq;
import com.yfy.app.net.judge.TeaJudgeGetAddTypeReq;
import com.yfy.app.net.judge.TeaJudgeGetTjDataReq;
import com.yfy.app.net.judge.TeaJudgeGetInfoDetailReq;
import com.yfy.app.net.judge.TeaJudgeGetTjTypeReq;
import com.yfy.app.net.judge.TeaJudgeGetAddParameterReq;
import com.yfy.app.net.judge.TeaJudgeGetYearReq;
import com.yfy.app.net.vote.VoteGetItemDetailReq;
import com.yfy.app.net.vote.VoteGetMainListReq;
import com.yfy.app.net.vote.VoteSubmitReq;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 */
@Root(name = TagFinal.BODY, strict = false)
public class ReqBody {

    //----------------------base----------



    @Element(name = TagFinal.BASE_SAVE_IMG, required = false)
    public SaveImgReq saveImgReq;

    @Element(name = TagFinal.BASE_GET_TOKEN, required = false)
    public BaseGetTokenReq baseGetTookenReq;

    @Element(name = TagFinal.USER_GET_CURRENT_TERM, required = false)
    public UserGetCurrentTermReq userGetCurrentTermReq;

    @Element(name = TagFinal.READNOTICE, required = false)
    public ReadNoticeReq readnotice;

    @Element(name = TagFinal.GETNOTICENUM, required = false)
    public NticeNumReq getnoticenum;

    @Element(name = TagFinal.USER_INFO, required = false)
    public UserGetBaseInfoReq userGetBaseInfoReq;



    //---------------------新生报名--------------

    @Element(name = TagFinal.AUTHENTICATION_GET_STU, required = false)
    public AuthenticationGetDataReq authenticationGetDataReq;
    @Element(name = TagFinal.AUTHENTICATION_LOGIN, required = false)
    public AuthenticationLoginReq authenticationLoginReq;

    /**
     * =======----------user--------------------
     */

    @Element(name = TagFinal.USER_GET_TERM_LIST, required = false)
    public UserGetTermListReq userGetTermListReq;

    @Element(name = TagFinal.USER_GET_ADMIN, required = false)
    public UserGetAdminReq userGetAdminReq;

    @Element(name = TagFinal.USER_GET_MOBILE, required = false)
    public UserGetCallReq userGetCallReq;

    @Element(name = TagFinal.USER_SET_MOBILE, required = false)
    public UserResetCallReq userResetCallReq;


    @Element(name = TagFinal.USER_ADD_HEAD, required = false)
    public UserResetPicReq userResetPicReq;
    //------------------------login--------------------
    @Element(name = TagFinal.USER_CHANGE_PASSWORD, required = false)
    public UserChangePasswordReq userChangePasswordReq;
    @Element(name = TagFinal.USER_RESET_PASSWORD, required = false)
    public UserResetPasswordReq userResetPasswordReq;

    @Element(name = TagFinal.USER_GET_CODE_TO_SYSTEM_PHONE, required = false)
    public PhoneCodeReq phoneCodeReq;

    @Element(name = TagFinal.USER_GET_CODE_TO_EDIT_PHONE, required = false)
    public ResetPasswordGetCodeReq resetPasswordGetCodeReq;

    @Element(name = TagFinal.USER_LOGIN, required = false)
    public UserLoginReq userLoginReq;

    @Element(name = TagFinal.USER_GET_DUPLICATION_LIST, required = false)
    public UserGetDuplicationListReq userGetDuplicationListReq;

    @Element(name = TagFinal.USER_DUPLICATION_LOGIN, required = false)
    public UserDuplicationLoginReq userDuplicationLoginReq;


    @Element(name = TagFinal.USER_LOGOUT, required = false)
    public UserLogoutReq userLogoutReq;

    @Element(name = TagFinal.USER_SIGN, required = false)
    public UserSignReq userSignReq;
    /**
     * -----------------affice school vote---------------
     */
    @Element(name = TagFinal.SCHOOL_GET_NEWS_LIST, required = false)
    public SchoolGetNewsListReq schoolGetNewsListReq;

    @Element(name = TagFinal.SCHOOL_GET_NEWS_BANNER, required = false)
    public SchoolGetBannerReq schoolBannerReq;

    @Element(name = TagFinal.SCHOOL_GET_MENU, required = false)
    public SchoolGetMenuReq schoolGetMenuReq;

    @Element(name = TagFinal.VOTE_GET_MAIN_LIST, required = false)
    public VoteGetMainListReq voteGetMainListReq;

    @Element(name = TagFinal.VOTE_GET_ITEM_DETAIL, required = false)
    public VoteGetItemDetailReq voteGetItemDetailReq;

    @Element(name = TagFinal.VOTE_SUBMIT, required = false)
    public VoteSubmitReq voteSubmitReq;
    /**
     * ---------------------foot book ---------------
     */
    @Element(name = TagFinal.FOOT_BOOK_GET_WEEK, required = false)
    public FootBookGetReq footBookGetReq;

    @Element(name = TagFinal.FOOT_BOOK_PRAISE, required = false)
    public FootBookPraiseReq footBookPraiseReq;

    @Element(name = TagFinal.FOOT_BOOK_GET_POPULAR, required = false)
    public FootBookGetPopularReq footBookGetPopularReq;

    @Element(name = TagFinal.FOOT_BOOK_ADD_SUGGEST, required = false)
    public FootBookAddSuggestReq footBookAddSuggestReq;
    /**
     * ---------------------book ---------------
     */

    @Element(name = TagFinal.VIDEO_GET_TAG, required = false)
    public VideoGetTypeReq videoGetTypeReq;

    @Element(name = TagFinal.VIDEO_GET_MAIN_LIST, required = false)
    public VideoGetUserListReq videoGetUserListReq;

    @Element(name = TagFinal.BOOK_GET_TAG, required = false)
    public BookGetTypeReq bookGetTypeReq;

    @Element(name = TagFinal.BOOK_GET_USER_LIST, required = false)
    public BookGetUserListReq bookGetUserListReq;

    /**
     * ---------------------大事记 event--------------
     */
    @Element(name = TagFinal.EVENT_GET_SECTION, required = false)
    public EventGetDepReq eventGetDepReq;

    @Element(name = TagFinal.EVENT_GET_WEEK, required = false)
    public EventGetWeekReq eventGetWeekReq;

    @Element(name = TagFinal.EVENT_GET_WEEK_LIST, required = false)
    public EventGetWeekListReq eventGetWeekListReq;

    @Element(name = TagFinal.EVENT_GET_DATE_LIST, required = false)
    public EventGetMainListReq eventGetMainListReq;

    @Element(name = TagFinal.EVENT_GET_USER_LIST, required = false)
    public EventGetMyListReq eventGetMyListReq;

    @Element(name = TagFinal.EVENT_DEL, required = false)
    public EventDelReq eventDelReq;

    @Element(name = TagFinal.EVENT_DEL_IMAGE, required = false)
    public EventDelImageReq eventDelImageReq;

    @Element(name = TagFinal.EVENT_ADD, required = false)
    public EventAddReq eventAddReq;


    //----------------------duty-------------


    @Element(name = TagFinal.DUTY_GET_USER, required = false)
    public DutyUserListReq dutyUserListReq;

    @Element(name = TagFinal.DUTY_TYPE, required = false)
    public DutyTypeReq duty_typeReq;

    @Element(name = TagFinal.USER_GET_WEEK_ALL, required = false)
    public WeekAllReq weekAllReq;

    @Element(name = TagFinal.DUTY_GET_PLANE, required = false)
    public DutyPlaneReq dutyPlaneReq;

    @Element(name = TagFinal.DUTY_GET_ADD_DETAILS, required = false)
    public DutyGetReportDetailReq dutyGetReportDetailReq;

    // ----------------notice-----------
    @Element(name = TagFinal.NOTICE_GET_USER_LIST, required = false)
    public NoticeGetUserListReq noticeGetUserListReq;

    @Element(name = TagFinal.NOTICE_GET_SEND_BOX_LIST, required = false)
    public NoticeGetSendBoxListReq noticeGetSendBoxListReq;

    @Element(name = TagFinal.NOTICE_GET_USER_DETAIL, required = false)
    public NoticeGetUserDetailReq noticeGetUserDetailReq;

    @Element(name = TagFinal.NOTICE_GET_SEND_BOX_DETAIL, required = false)
    public NoticeGetSendBoxDetailReq noticeGetSendBoxDetailReq;

    @Element(name = TagFinal.NOTICE_READ, required = false)
    public NoticeReadReq noticeReadReq;

    @Element(name = TagFinal.NOTICE_GET_TEA, required = false)
    public NoticeGetTeaListReq noticeGetTeaListReq;

    @Element(name = TagFinal.NOTICE_GET_STU, required = false)
    public NoticeGetStuListReq noticeGetStuListReq;
//
//    @Element(name = TagFinal.NOTICE_GET_TEA_CONTACTS, required = false)
//    public NoticeGetTeaConListReq noticeGetTeaConListReq;

    @Element(name = TagFinal.NOTICE_SEND, required = false)
    public NoticeAddReq noticeAddReq;


    //-------------------------maintain------------------

    @Element(name = TagFinal.MAINTENANCE_DELETE_MAINTAIN, required = false)
    public MaintainDelItemReq maintainDelItemReq;

    @Element(name = TagFinal.MAINTENANCE_GET_COUNT, required = false)
    public MaintainGetCountReq maintainGetCountReq;

    @Element(name = TagFinal.MAINTENANCE_GET_MAIN_LIST_USER, required = false)
    public MaintainGetUserListReq maintainGetUserListReq;

    @Element(name = TagFinal.MAINTENANCE_GET_MAIN_LIST_ADMIN, required = false)
    public MaintainGetAdminListReq maintainGetAdminListReq;

    @Element(name = TagFinal.MAINTENANCE_GET_SECTION, required = false)
    public MaintainGetSectionReq maintainGetSectionReq;

    @Element(name = TagFinal.MAINTENANCE_SET_SECTION, required = false)
    public MaintainSetSectionReq maintainSetSectionReq;

    @Element(name = TagFinal.MAINTENANCE_ADD, required = false)
    public MaintainApplyReq maintainApplyReq;

    @Element(name = TagFinal.MAINTENANCE_ADMIN_SET_STATE, required = false)
    public MaintainAdminSetStateReq maintainAdminSetStateReq;

    @Element(name = TagFinal.BOSS_MAINTAIN_LIST_DETAIL, required = false)
    public BMaintainDetailReq bMaintainDetailReq;

    @Element(name = TagFinal.BOSS_MAINTAIN_LIST, required = false)
    public BMaintainMainReq bMaintainMainReq;

    //    -------------------------atten---------------


    @Element(name = TagFinal.ATTENDANCE_APPLY, required = false)
    public AttenApplyReq attenApplyReq;

    @Element(name = TagFinal.ATTENDANCE_GET_COUNT, required = false)
    public AttenGetCountReq attenGetCountReq;

    @Element(name = TagFinal.ATTENDANCE_ADMIN_DO, required = false)
    public AttenAdminDoingReq attenAdminDoingReq;

    @Element(name = TagFinal.ATTENDANCE_DELETE, required = false)
    public AttenDelItemReq attenDelItemReq;

    @Element(name = TagFinal.ATTENDANCE_GET_TYPE, required = false)
    public AttenGetTypeReq attenGetTypeReq;

    @Element(name = TagFinal.ATTENDANCE_GET_AUDITOR_LIST, required = false)
    public AttenGetAuditorListReq attenGetAuditorListReq;

    @Element(name = TagFinal.ATTENDANCE_GET_ADMIN_LIST, required = false)
    public AttenGetAdminListReq attenGetAdminListReq;

    @Element(name = TagFinal.ATTENDANCE_GET_USER_LIST, required = false)
    public AttenGetUserListReq attenGetUserListReq;

    @Element(name = TagFinal.BOSS_ATTEN_LIST, required = false)
    public BAttenMainReq bAttenMainReq;

    //---------------------applied_order----------------
    @Element(name = TagFinal.ORDER_GET_COUNT, required = false)
    public OrderGetCountReq orderGetCountReq;

    @Element(name = TagFinal.ORDER_GET_USER_LIST, required = false)
    public OrderGetUserListReq orderGetUserListReq;

    @Element(name = TagFinal.ORDER_GET_ADMIN_LIST, required = false)
    public OrderGetAdminListReq orderGetAdminListReq;

    @Element(name = TagFinal.ORDER_GET_LOGISTICS_LIST, required = false)
    public OrderGetLogisticsListReq orderGetLogisticsListReq;

    @Element(name = TagFinal.ORDER_GET_DETAIL, required = false)
    public OrderGetDetailReq orderGetDetailReq;

    @Element(name = TagFinal.ORDER_ADD, required = false)
    public OrderApplyNewReq orderApplyNewReq;

    @Element(name = TagFinal.ORDER_GET_ROOM_NAME, required = false)
    public OrderGetRoomNameReq orderGetRoomNameReq;

    @Element(name = TagFinal.ORDER_QUERY, required = false)
    public OrderQueryRoomDayReq orderQueryRoomDayReq;

    @Element(name = TagFinal.ORDER_GET_APPLY_GRADE, required = false)
    public OrderGetApplyGradeReq orderGetApplyGradeReq;

    @Element(name = TagFinal.ORDER_ADMIN_SET, required = false)
    public OrderAdminSetReq orderAdminSetReq;

    @Element(name = TagFinal.ORDER_SET_SCORE, required = false)
    public OrderSetScoreReq orderSetScoreReq;

    @Element(name = TagFinal.BOSS_ORDER_LIST, required = false)
    public BOrderMainReq bOrderMainReq;


    //----------------tea evaluate----------------------


    //----------------tea evaluate----------------------


    @Element(name = TagFinal.TEA_JUDGE_GET_ADD_TYPE, required = false)
    public TeaJudgeGetAddTypeReq teaJudgeGetAddTypeReq;

    @Element(name = TagFinal.TEA_JUDGE_GET_STATISTICS_TYPE, required = false)
    public TeaJudgeGetTjTypeReq teaJudgeGetTjTypeReq;

    @Element(name = TagFinal.TEA_JUDGE_GET_STATISTICS_DATA, required = false)
    public TeaJudgeGetTjDataReq teaJudgeGetTjDataReq;

    @Element(name = TagFinal.TEA_JUDGE_GET_YEAR, required = false)
    public TeaJudgeGetYearReq teaJudgeGetYearReq;

    @Element(name = TagFinal.TEA_JUDGE_GET_ADD_PARAMETER, required = false)
    public TeaJudgeGetAddParameterReq teaJudgeGetAddParameterReq;

    @Element(name = TagFinal.TEA_JUDGE_GET_INFO_DETAIL, required = false)
    public TeaJudgeGetInfoDetailReq teaJudgeGetInfoDetailReq;

    @Element(name = TagFinal.TEA_JUDGE_GET_INFO_YEAR_LIST, required = false)
    public TeaJudgeGetInfoYearListReq teaJudgeGetInfoYearListReq;

    @Element(name = TagFinal.TEA_JUDGE_DEL_PIC, required = false)
    public TeaJudgeDelImageReq teaJudgeDelImageReq;

    @Element(name = TagFinal.TEA_JUDGE_DEL_ITEM, required = false)
    public TeaJudgeDelItemReq teaJudgeDelItemReq;

    @Element(name = TagFinal.TEA_JUDGE_ADD, required = false)
    public TeaJudgeAddReq teaJudgeAddReq;

    //---------------------------satisfaction------------------------

    @Element(name = TagFinal.SATISFACTION_STU_GET_TEAS, required = false)
    public SatisfactionStuGetTeaReq satisfactionStuGetTeaReq;

    @Element(name = TagFinal.SATISFACTION_STU_GET_SCHOOL, required = false)
    public SatisfactionStuGetSchoolReq satisfactionStuGetSchoolReq;

    @Element(name = TagFinal.SATISFACTION_STU_SET_SCHOOL_SCORE, required = false)
    public SatisfactionStuSetSchoolScoreReq satisfactionStuSetSchoolScoreReq;

    @Element(name = TagFinal.SATISFACTION_STU_SET_TEA_SCORE, required = false)
    public SatisfactionStuSetTeaScoreReq satisfactionStuSetTeaScoreReq;

    @Element(name = TagFinal.SATISFACTION_GET_DETAILS, required = false)
    public SatisfactionGetDetailReq satisfactionGetDetailReq;

    @Element(name = TagFinal.SATISFACTION_TEA_GET_STU, required = false)
    public SatisfactionTeaGetStuReq satisfactionTeaGetStuReq;

    @Element(name = TagFinal.SATISFACTION_TEA_GET_MAIN, required = false)
    public SatisfactionTeaGetMainReq satisfactionTeaGetMainReq;

    //------------goods-------------------
    @Element(name = TagFinal.GOODS_GET_COUNT_ADMIN, required = false)
    public GoodGetCountAdminReq goodGetCountAdminReq;

    @Element(name = TagFinal.GOODS_GET_COUNT_SCHOOL, required = false)
    public GoodGetCountSchoolReq goodGetCountSchoolReq;

    @Element(name = TagFinal.GOODS_GET_COUNT_MASTER, required = false)
    public GoodGetCountMasterReq goodGetCountMasterReq;

    @Element(name = TagFinal.GOODS_GET_USER_RECORD_LIST, required = false)
    public GoodGetRecordListUserReq goodGetRecordListUserReq;

    @Element(name = TagFinal.GOODS_GET_ADMIN_RECORD_LIST, required = false)
    public GoodGetRecordListAdminReq goodGetRecordListAdminReq;

    @Element(name = TagFinal.GOODS_GET_MASTER_RECORD_LIST, required = false)
    public GoodGetRecordListMasterReq goodGetRecordListMasterReq;

    @Element(name = TagFinal.GOODS_GET_SCHOOL_RECORD_LIST, required = false)
    public GoodGetRecordListSchoolReq goodGetRecordListSchoolReq;

    @Element(name = TagFinal.GOODS_GET_ITEM_DETAILS, required = false)
    public GoodGetItemDetailReq goodGetItemDetailReq;

    @Element(name = TagFinal.GOODS_GET_STATIONERY_TYPE, required = false)
    public GoodGetStationeryTypeReq goodGetStationeryTypeReq;

    @Element(name = TagFinal.GOODS_GET_MASTER_USER, required = false)
    public GoodGetMasterUserReq goodGetMasterUserReq;

    @Element(name = TagFinal.GOODS_DELETE_ITEM, required = false)
    public GoodDelUserItemReq goodDelUserItemReq;

    @Element(name = TagFinal.GOODS_ADD_APPLY, required = false)
    public GoodAddApplyReq goodAddApplyReq;

    @Element(name = TagFinal.GOODS_ADD_STATIONERY_TYPE, required = false)
    public GoodAddStationeryTypeReq goodAddStationeryTypeReq;

    @Element(name = TagFinal.GOODS_ADMIN_APPLY, required = false)
    public GoodAdminApplyReq goodAdminApplyReq;

    @Element(name = TagFinal.GOODS_SEARCH_GET_STATIONERY_TYPE, required = false)
    public GoodSearchGetStationeryTypeReq goodSearchGetStationeryTypeReq;


    @Element(name = TagFinal.GOODS_NUM_GET_COUNT, required = false)
    public GoodNumCountReq goodNumCountReq;

    @Element(name = TagFinal.GOODS_NUM_ADD, required = false)
    public GoodNumAddReq goodNumAddReq;

    @Element(name = TagFinal.GOODS_NUM_GET_BYID, required = false)
    public GoodNumGetByIdReq goodNumGetByIdReq;

    @Element(name = TagFinal.GOODS_NUM_GET_USER_LIST, required = false)
    public GoodNumUserListReq goodNumUserListReq;

    @Element(name = TagFinal.GOODS_NUM_GET_ADMIN_LIST, required = false)
    public GoodNumAdminListReq goodNumAdminListReq;

    @Element(name = TagFinal.GOODS_NUM_DO_STATE, required = false)
    public GoodNumDoReq goodNumDoReq;

    //------------check---------------------

    @Element(name = TagFinal.CHECK_GET_TYPE, required = false)
    public CheckGetTypeReq checkGetTypeReq;
    @Element(name = TagFinal.CHECK_GET_ILL_TYPE, required = false)
    public CheckGetIllTypeReq checkGetIllTypeReq;
    @Element(name = TagFinal.CHECK_GET_CLASS, required = false)
    public CheckGetClassReq checkGetClassReq;

    @Element(name = TagFinal.CHECK_GET_STU, required = false)
    public CheckGetStuReq checkGetStuReq;
    @Element(name = TagFinal.CHECK_GET_STU_TO_TYPE_ILL, required = false)
    public CheckGetStuToIllReq checkGetStuToIllReq;

    @Element(name = TagFinal.CHECK_GET_STU_ITEM_DETAIL, required = false)
    public CheckStuParentDetailReq checkStuParentDetailReq;

    @Element(name = TagFinal.CHECK_GET_STU_ALL_DETAIL, required = false)
    public CheckStuChildDetailReq checkStuChildDetailReq;

    @Element(name = TagFinal.CHECK_SUBMIT_YES, required = false)
    public CheckSubimtYesReq checkSubimtYesReq;

    @Element(name = TagFinal.CHECK_ADD_CHILD, required = false)
    public CheckAddChildReq checkAddChildReq;

    @Element(name = TagFinal.CHECK_ADD_PARENT, required = false)
    public CheckAddParentReq checkAddParentReq;

    @Element(name = TagFinal.CHECK_DEL_CHILD, required = false)
    public CheckStuDelChildReq checkStuDelChildReq;

    @Element(name = TagFinal.CHECK_DEL_PARENT, required = false)
    public CheckStuDelParentReq checkStuDelParentReq;

    @Element(name = TagFinal.CHECK_GET_TJ, required = false)
    public CheckTjListReq checkTjListReq;

    //----------------------seal_----------------------------
    @Element(name = TagFinal.SEAL_GET_SEAL_TYPE, required = false)
    public SealGetSealTypeReq sealGetSealTypeReq;

    @Element(name = TagFinal.SEAL_GET_USER_LIST, required = false)
    public SealUserListReq sealUserListReq;

    @Element(name = TagFinal.SEAL_GET_ADMIN_LIST, required = false)
    public SealAdminListReq sealAdminListReq;

    @Element(name = TagFinal.SEAL_GET_MASTER_COUNT, required = false)
    public SealMasterCountReq sealMasterCountReq;

    @Element(name = TagFinal.SEAL_GET_APPLY_MASTER_LIST, required = false)
    public SealApplyMasterUserReq sealApplyMasterUserReq;

    @Element(name = TagFinal.SEAL_SUBMIT, required = false)
    public SealApplyAddReq sealApplyAddReq;
    @Element(name = TagFinal.SEAL_GET_ITEM_BYID, required = false)
    public SealByIdItemReq sealByIdItemReq;
    @Element(name = TagFinal.SEAL_GET_ALL, required = false)
    public SealAllDetailReq sealAllDetailReq;
    @Element(name = TagFinal.SEAL_DO, required = false)
    public SealSetDoReq sealSetDoReq;

    //----------------------------delay---------------

    @Element(name = TagFinal.DELAY_ADMIN_GET_TODAY_LIST, required = false)
    public DelayAdminGetTodayListReq delayAdminGetTodayListReq;

    @Element(name = TagFinal.DELAY_ADMIN_GET_CLASS_LIST, required = false)
    public DelayAdminGetClassListReq delayAdminGetClassListReq;

    @Element(name = TagFinal.DELAY_ADMIN_get_TO_CLASS_EVENT_DETAIL, required = false)
    public DelayAdminGetToClassEventDetailReq delayAdminGetToClassEventDetailReq;

    @Element(name = TagFinal.DELAY_ADMIN_CLASS_SET, required = false)
    public DelayAdminClassSetReq delayAdminClassSetReq;

    @Element(name = TagFinal.DELAY_GET_TYPE, required = false)
    public DelayGetTypeReq delayGetTypeReq;

    @Element(name = TagFinal.DELAY_GET_ITEM_DETAIL, required = false)
    public DelayGetItemDetailReq delayGetItemDetailReq;

    @Element(name = TagFinal.DELAY_GET_CLASS_STU_LIST, required = false)
    public DelayGetClassStuListReq delayGetClassStuListReq;

    @Element(name = TagFinal.DELAY_DEL, required = false)
    public DelayDelStuItemReq delayDelStuItemReq;

    @Element(name = TagFinal.DELAY_GET_CLASS_LIST_TEA, required = false)
    public DelayGetClassListTeaReq delayGetClassListTeaReq;

    @Element(name = TagFinal.DELAY_GET_CLASS_LIST_REPLACE, required = false)
    public DelayGetClassListReplaceReq delayGetClassListReplaceReq;

    @Element(name = TagFinal.DELAY_GET_COPY_LIST, required = false)
    public DelayGetCopyListReq delayGetCopyListReq;

    @Element(name = TagFinal.DELAY_COPY_SET, required = false)
    public DelayCopySetReq delayCopySetReq;

    @Element(name = TagFinal.DELAY_TEA_ADD, required = false)
    public DelayTeaAddReq delayTeaAddReq;


    //----------------process----------------------
    @Element(name = TagFinal.PROCESS_GET_TYPE, required = false)
    public ProcessGetTypeReq processGetTypeReq;

    @Element(name = TagFinal.PROCESS_GET_TERM, required = false)
    public ProcessGetTermReq processGetTermReq;

    @Element(name = TagFinal.PROCESS_GET_GROUP, required = false)
    public ProcessGetGroupReq processGetGroupReq;

    @Element(name = TagFinal.PROCESS_GET_USER_COUNT, required = false)
    public ProcessGetUserCountReq processGetUserCountReq;

    @Element(name = TagFinal.PROCESS_ADD, required = false)
    public ProcessApplyDetailReq processApplyDetailReq;

    @Element(name = TagFinal.PROCESS_GET_USER_DATA_LIST, required = false)
    public ProcessGetUserListReq processGetUserListReq;

    @Element(name = TagFinal.PROCESS_GET_ITEM_DETAIL, required = false)
    public ProcessGetDetailReq processGetDetailReq;

    @Element(name = TagFinal.PROCESS_DEL_ITEM_DETAIL, required = false)
    public ProcessDelDetailReq processDelDetailReq;

    @Element(name = TagFinal.PROCESS_ADMIN_GET_DATA_LIST, required = false)
    public ProcessAdminGetListReq processAdminGetListReq;

    @Element(name = TagFinal.PROCESS_ADMIN_SET_CLASS, required = false)
    public ProcessAdminSetClassReq processAdminSetClassReq;

    @Element(name = TagFinal.PROCESS_ADMIN_SET_STATE, required = false)
    public ProcessAdminSetStateReq processAdminSetStateReq;
























    public void setSaveImgReq(SaveImgReq saveImgReq) {
        this.saveImgReq = saveImgReq;
    }

    public void setBaseGetTookenReq(BaseGetTokenReq baseGetTookenReq) {
        this.baseGetTookenReq = baseGetTookenReq;
    }

    public void setUserGetCurrentTermReq(UserGetCurrentTermReq userGetCurrentTermReq) {
        this.userGetCurrentTermReq = userGetCurrentTermReq;
    }

    public void setReadnotice(ReadNoticeReq readnotice) {
        this.readnotice = readnotice;
    }

    public void setGetnoticenum(NticeNumReq getnoticenum) {
        this.getnoticenum = getnoticenum;
    }



    public void setUserGetTermListReq(UserGetTermListReq userGetTermListReq) {
        this.userGetTermListReq = userGetTermListReq;
    }

    public void setUserGetAdminReq(UserGetAdminReq userGetAdminReq) {
        this.userGetAdminReq = userGetAdminReq;
    }

    public void setUserGetCallReq(UserGetCallReq userGetCallReq) {
        this.userGetCallReq = userGetCallReq;
    }

    public void setUserResetCallReq(UserResetCallReq userResetCallReq) {
        this.userResetCallReq = userResetCallReq;
    }

    public void setUserResetPicReq(UserResetPicReq userResetPicReq) {
        this.userResetPicReq = userResetPicReq;
    }

    public void setUserChangePasswordReq(UserChangePasswordReq userChangePasswordReq) {
        this.userChangePasswordReq = userChangePasswordReq;
    }

    public void setUserResetPasswordReq(UserResetPasswordReq userResetPasswordReq) {
        this.userResetPasswordReq = userResetPasswordReq;
    }

    public void setPhoneCodeReq(PhoneCodeReq phoneCodeReq) {
        this.phoneCodeReq = phoneCodeReq;
    }

    public void setResetPasswordGetCodeReq(ResetPasswordGetCodeReq resetPasswordGetCodeReq) {
        this.resetPasswordGetCodeReq = resetPasswordGetCodeReq;
    }

    public void setUserLoginReq(UserLoginReq userLoginReq) {
        this.userLoginReq = userLoginReq;
    }

    public void setUserGetDuplicationListReq(UserGetDuplicationListReq userGetDuplicationListReq) {
        this.userGetDuplicationListReq = userGetDuplicationListReq;
    }

    public void setUserDuplicationLoginReq(UserDuplicationLoginReq userDuplicationLoginReq) {
        this.userDuplicationLoginReq = userDuplicationLoginReq;
    }

    public void setUserLogoutReq(UserLogoutReq userLogoutReq) {
        this.userLogoutReq = userLogoutReq;
    }

    public void setUserSignReq(UserSignReq userSignReq) {
        this.userSignReq = userSignReq;
    }

    public void setSchoolGetNewsListReq(SchoolGetNewsListReq schoolGetNewsListReq) {
        this.schoolGetNewsListReq = schoolGetNewsListReq;
    }

    public void setSchoolBannerReq(SchoolGetBannerReq schoolBannerReq) {
        this.schoolBannerReq = schoolBannerReq;
    }

    public void setSchoolGetMenuReq(SchoolGetMenuReq schoolGetMenuReq) {
        this.schoolGetMenuReq = schoolGetMenuReq;
    }

    public void setVoteGetMainListReq(VoteGetMainListReq voteGetMainListReq) {
        this.voteGetMainListReq = voteGetMainListReq;
    }

    public void setVoteGetItemDetailReq(VoteGetItemDetailReq voteGetItemDetailReq) {
        this.voteGetItemDetailReq = voteGetItemDetailReq;
    }

    public void setVoteSubmitReq(VoteSubmitReq voteSubmitReq) {
        this.voteSubmitReq = voteSubmitReq;
    }

    public void setFootBookGetReq(FootBookGetReq footBookGetReq) {
        this.footBookGetReq = footBookGetReq;
    }

    public void setFootBookPraiseReq(FootBookPraiseReq footBookPraiseReq) {
        this.footBookPraiseReq = footBookPraiseReq;
    }

    public void setFootBookGetPopularReq(FootBookGetPopularReq footBookGetPopularReq) {
        this.footBookGetPopularReq = footBookGetPopularReq;
    }

    public void setFootBookAddSuggestReq(FootBookAddSuggestReq footBookAddSuggestReq) {
        this.footBookAddSuggestReq = footBookAddSuggestReq;
    }

    public void setVideoGetTypeReq(VideoGetTypeReq videoGetTypeReq) {
        this.videoGetTypeReq = videoGetTypeReq;
    }

    public void setVideoGetUserListReq(VideoGetUserListReq videoGetUserListReq) {
        this.videoGetUserListReq = videoGetUserListReq;
    }

    public void setBookGetTypeReq(BookGetTypeReq bookGetTypeReq) {
        this.bookGetTypeReq = bookGetTypeReq;
    }

    public void setBookGetUserListReq(BookGetUserListReq bookGetUserListReq) {
        this.bookGetUserListReq = bookGetUserListReq;
    }

    public void setEventGetDepReq(EventGetDepReq eventGetDepReq) {
        this.eventGetDepReq = eventGetDepReq;
    }

    public void setEventGetWeekReq(EventGetWeekReq eventGetWeekReq) {
        this.eventGetWeekReq = eventGetWeekReq;
    }

    public void setEventGetWeekListReq(EventGetWeekListReq eventGetWeekListReq) {
        this.eventGetWeekListReq = eventGetWeekListReq;
    }

    public void setEventGetMainListReq(EventGetMainListReq eventGetMainListReq) {
        this.eventGetMainListReq = eventGetMainListReq;
    }

    public void setEventGetMyListReq(EventGetMyListReq eventGetMyListReq) {
        this.eventGetMyListReq = eventGetMyListReq;
    }

    public void setEventDelReq(EventDelReq eventDelReq) {
        this.eventDelReq = eventDelReq;
    }

    public void setEventDelImageReq(EventDelImageReq eventDelImageReq) {
        this.eventDelImageReq = eventDelImageReq;
    }

    public void setEventAddReq(EventAddReq eventAddReq) {
        this.eventAddReq = eventAddReq;
    }

    public void setDutyUserListReq(DutyUserListReq dutyUserListReq) {
        this.dutyUserListReq = dutyUserListReq;
    }

    public void setDuty_typeReq(DutyTypeReq duty_typeReq) {
        this.duty_typeReq = duty_typeReq;
    }

    public void setWeekAllReq(WeekAllReq weekAllReq) {
        this.weekAllReq = weekAllReq;
    }

    public void setDutyPlaneReq(DutyPlaneReq dutyPlaneReq) {
        this.dutyPlaneReq = dutyPlaneReq;
    }

    public void setNoticeGetUserListReq(NoticeGetUserListReq noticeGetUserListReq) {
        this.noticeGetUserListReq = noticeGetUserListReq;
    }

    public void setNoticeGetSendBoxListReq(NoticeGetSendBoxListReq noticeGetSendBoxListReq) {
        this.noticeGetSendBoxListReq = noticeGetSendBoxListReq;
    }

    public void setNoticeGetUserDetailReq(NoticeGetUserDetailReq noticeGetUserDetailReq) {
        this.noticeGetUserDetailReq = noticeGetUserDetailReq;
    }

    public void setNoticeGetSendBoxDetailReq(NoticeGetSendBoxDetailReq noticeGetSendBoxDetailReq) {
        this.noticeGetSendBoxDetailReq = noticeGetSendBoxDetailReq;
    }

    public void setNoticeReadReq(NoticeReadReq noticeReadReq) {
        this.noticeReadReq = noticeReadReq;
    }

    public void setNoticeGetTeaListReq(NoticeGetTeaListReq noticeGetTeaListReq) {
        this.noticeGetTeaListReq = noticeGetTeaListReq;
    }

    public void setNoticeGetStuListReq(NoticeGetStuListReq noticeGetStuListReq) {
        this.noticeGetStuListReq = noticeGetStuListReq;
    }

    public void setNoticeAddReq(NoticeAddReq noticeAddReq) {
        this.noticeAddReq = noticeAddReq;
    }

    public void setMaintainDelItemReq(MaintainDelItemReq maintainDelItemReq) {
        this.maintainDelItemReq = maintainDelItemReq;
    }

    public void setMaintainGetCountReq(MaintainGetCountReq maintainGetCountReq) {
        this.maintainGetCountReq = maintainGetCountReq;
    }

    public void setMaintainGetUserListReq(MaintainGetUserListReq maintainGetUserListReq) {
        this.maintainGetUserListReq = maintainGetUserListReq;
    }

    public void setMaintainGetAdminListReq(MaintainGetAdminListReq maintainGetAdminListReq) {
        this.maintainGetAdminListReq = maintainGetAdminListReq;
    }

    public void setMaintainGetSectionReq(MaintainGetSectionReq maintainGetSectionReq) {
        this.maintainGetSectionReq = maintainGetSectionReq;
    }

    public void setMaintainSetSectionReq(MaintainSetSectionReq maintainSetSectionReq) {
        this.maintainSetSectionReq = maintainSetSectionReq;
    }

    public void setMaintainApplyReq(MaintainApplyReq maintainApplyReq) {
        this.maintainApplyReq = maintainApplyReq;
    }

    public void setMaintainAdminSetStateReq(MaintainAdminSetStateReq maintainAdminSetStateReq) {
        this.maintainAdminSetStateReq = maintainAdminSetStateReq;
    }

    public void setbMaintainDetailReq(BMaintainDetailReq bMaintainDetailReq) {
        this.bMaintainDetailReq = bMaintainDetailReq;
    }

    public void setbMaintainMainReq(BMaintainMainReq bMaintainMainReq) {
        this.bMaintainMainReq = bMaintainMainReq;
    }

    public void setAttenApplyReq(AttenApplyReq attenApplyReq) {
        this.attenApplyReq = attenApplyReq;
    }

    public void setAttenGetCountReq(AttenGetCountReq attenGetCountReq) {
        this.attenGetCountReq = attenGetCountReq;
    }

    public void setAttenAdminDoingReq(AttenAdminDoingReq attenAdminDoingReq) {
        this.attenAdminDoingReq = attenAdminDoingReq;
    }

    public void setAttenDelItemReq(AttenDelItemReq attenDelItemReq) {
        this.attenDelItemReq = attenDelItemReq;
    }

    public void setAttenGetTypeReq(AttenGetTypeReq attenGetTypeReq) {
        this.attenGetTypeReq = attenGetTypeReq;
    }

    public void setAttenGetAuditorListReq(AttenGetAuditorListReq attenGetAuditorListReq) {
        this.attenGetAuditorListReq = attenGetAuditorListReq;
    }

    public void setAttenGetAdminListReq(AttenGetAdminListReq attenGetAdminListReq) {
        this.attenGetAdminListReq = attenGetAdminListReq;
    }

    public void setAttenGetUserListReq(AttenGetUserListReq attenGetUserListReq) {
        this.attenGetUserListReq = attenGetUserListReq;
    }

    public void setbAttenMainReq(BAttenMainReq bAttenMainReq) {
        this.bAttenMainReq = bAttenMainReq;
    }

    public void setOrderGetCountReq(OrderGetCountReq orderGetCountReq) {
        this.orderGetCountReq = orderGetCountReq;
    }

    public void setOrderGetUserListReq(OrderGetUserListReq orderGetUserListReq) {
        this.orderGetUserListReq = orderGetUserListReq;
    }

    public void setOrderGetAdminListReq(OrderGetAdminListReq orderGetAdminListReq) {
        this.orderGetAdminListReq = orderGetAdminListReq;
    }

    public void setOrderGetLogisticsListReq(OrderGetLogisticsListReq orderGetLogisticsListReq) {
        this.orderGetLogisticsListReq = orderGetLogisticsListReq;
    }

    public void setOrderGetDetailReq(OrderGetDetailReq orderGetDetailReq) {
        this.orderGetDetailReq = orderGetDetailReq;
    }

    public void setOrderApplyNewReq(OrderApplyNewReq orderApplyNewReq) {
        this.orderApplyNewReq = orderApplyNewReq;
    }

    public void setOrderGetRoomNameReq(OrderGetRoomNameReq orderGetRoomNameReq) {
        this.orderGetRoomNameReq = orderGetRoomNameReq;
    }

    public void setOrderQueryRoomDayReq(OrderQueryRoomDayReq orderQueryRoomDayReq) {
        this.orderQueryRoomDayReq = orderQueryRoomDayReq;
    }

    public void setOrderGetApplyGradeReq(OrderGetApplyGradeReq orderGetApplyGradeReq) {
        this.orderGetApplyGradeReq = orderGetApplyGradeReq;
    }

    public void setOrderAdminSetReq(OrderAdminSetReq orderAdminSetReq) {
        this.orderAdminSetReq = orderAdminSetReq;
    }

    public void setOrderSetScoreReq(OrderSetScoreReq orderSetScoreReq) {
        this.orderSetScoreReq = orderSetScoreReq;
    }

    public void setbOrderMainReq(BOrderMainReq bOrderMainReq) {
        this.bOrderMainReq = bOrderMainReq;
    }

    public void setTeaJudgeGetAddTypeReq(TeaJudgeGetAddTypeReq teaJudgeGetAddTypeReq) {
        this.teaJudgeGetAddTypeReq = teaJudgeGetAddTypeReq;
    }

    public void setTeaJudgeGetTjTypeReq(TeaJudgeGetTjTypeReq teaJudgeGetTjTypeReq) {
        this.teaJudgeGetTjTypeReq = teaJudgeGetTjTypeReq;
    }

    public void setTeaJudgeGetTjDataReq(TeaJudgeGetTjDataReq teaJudgeGetTjDataReq) {
        this.teaJudgeGetTjDataReq = teaJudgeGetTjDataReq;
    }

    public void setTeaJudgeGetYearReq(TeaJudgeGetYearReq teaJudgeGetYearReq) {
        this.teaJudgeGetYearReq = teaJudgeGetYearReq;
    }

    public void setTeaJudgeGetAddParameterReq(TeaJudgeGetAddParameterReq teaJudgeGetAddParameterReq) {
        this.teaJudgeGetAddParameterReq = teaJudgeGetAddParameterReq;
    }

    public void setTeaJudgeGetInfoDetailReq(TeaJudgeGetInfoDetailReq teaJudgeGetInfoDetailReq) {
        this.teaJudgeGetInfoDetailReq = teaJudgeGetInfoDetailReq;
    }

    public void setTeaJudgeGetInfoYearListReq(TeaJudgeGetInfoYearListReq teaJudgeGetInfoYearListReq) {
        this.teaJudgeGetInfoYearListReq = teaJudgeGetInfoYearListReq;
    }

    public void setTeaJudgeDelImageReq(TeaJudgeDelImageReq teaJudgeDelImageReq) {
        this.teaJudgeDelImageReq = teaJudgeDelImageReq;
    }

    public void setTeaJudgeDelItemReq(TeaJudgeDelItemReq teaJudgeDelItemReq) {
        this.teaJudgeDelItemReq = teaJudgeDelItemReq;
    }

    public void setTeaJudgeAddReq(TeaJudgeAddReq teaJudgeAddReq) {
        this.teaJudgeAddReq = teaJudgeAddReq;
    }

    public void setSatisfactionStuGetTeaReq(SatisfactionStuGetTeaReq satisfactionStuGetTeaReq) {
        this.satisfactionStuGetTeaReq = satisfactionStuGetTeaReq;
    }

    public void setSatisfactionStuGetSchoolReq(SatisfactionStuGetSchoolReq satisfactionStuGetSchoolReq) {
        this.satisfactionStuGetSchoolReq = satisfactionStuGetSchoolReq;
    }

    public void setSatisfactionStuSetSchoolScoreReq(SatisfactionStuSetSchoolScoreReq satisfactionStuSetSchoolScoreReq) {
        this.satisfactionStuSetSchoolScoreReq = satisfactionStuSetSchoolScoreReq;
    }

    public void setSatisfactionStuSetTeaScoreReq(SatisfactionStuSetTeaScoreReq satisfactionStuSetTeaScoreReq) {
        this.satisfactionStuSetTeaScoreReq = satisfactionStuSetTeaScoreReq;
    }

    public void setSatisfactionGetDetailReq(SatisfactionGetDetailReq satisfactionGetDetailReq) {
        this.satisfactionGetDetailReq = satisfactionGetDetailReq;
    }

    public void setSatisfactionTeaGetStuReq(SatisfactionTeaGetStuReq satisfactionTeaGetStuReq) {
        this.satisfactionTeaGetStuReq = satisfactionTeaGetStuReq;
    }

    public void setSatisfactionTeaGetMainReq(SatisfactionTeaGetMainReq satisfactionTeaGetMainReq) {
        this.satisfactionTeaGetMainReq = satisfactionTeaGetMainReq;
    }

    public void setGoodGetCountAdminReq(GoodGetCountAdminReq goodGetCountAdminReq) {
        this.goodGetCountAdminReq = goodGetCountAdminReq;
    }

    public void setGoodGetCountSchoolReq(GoodGetCountSchoolReq goodGetCountSchoolReq) {
        this.goodGetCountSchoolReq = goodGetCountSchoolReq;
    }

    public void setGoodGetCountMasterReq(GoodGetCountMasterReq goodGetCountMasterReq) {
        this.goodGetCountMasterReq = goodGetCountMasterReq;
    }

    public void setGoodGetRecordListUserReq(GoodGetRecordListUserReq goodGetRecordListUserReq) {
        this.goodGetRecordListUserReq = goodGetRecordListUserReq;
    }

    public void setGoodGetRecordListAdminReq(GoodGetRecordListAdminReq goodGetRecordListAdminReq) {
        this.goodGetRecordListAdminReq = goodGetRecordListAdminReq;
    }

    public void setGoodGetRecordListMasterReq(GoodGetRecordListMasterReq goodGetRecordListMasterReq) {
        this.goodGetRecordListMasterReq = goodGetRecordListMasterReq;
    }

    public void setGoodGetRecordListSchoolReq(GoodGetRecordListSchoolReq goodGetRecordListSchoolReq) {
        this.goodGetRecordListSchoolReq = goodGetRecordListSchoolReq;
    }

    public void setGoodGetItemDetailReq(GoodGetItemDetailReq goodGetItemDetailReq) {
        this.goodGetItemDetailReq = goodGetItemDetailReq;
    }

    public void setGoodGetStationeryTypeReq(GoodGetStationeryTypeReq goodGetStationeryTypeReq) {
        this.goodGetStationeryTypeReq = goodGetStationeryTypeReq;
    }

    public void setGoodGetMasterUserReq(GoodGetMasterUserReq goodGetMasterUserReq) {
        this.goodGetMasterUserReq = goodGetMasterUserReq;
    }

    public void setGoodDelUserItemReq(GoodDelUserItemReq goodDelUserItemReq) {
        this.goodDelUserItemReq = goodDelUserItemReq;
    }

    public void setGoodAddApplyReq(GoodAddApplyReq goodAddApplyReq) {
        this.goodAddApplyReq = goodAddApplyReq;
    }

    public void setGoodAddStationeryTypeReq(GoodAddStationeryTypeReq goodAddStationeryTypeReq) {
        this.goodAddStationeryTypeReq = goodAddStationeryTypeReq;
    }

    public void setGoodAdminApplyReq(GoodAdminApplyReq goodAdminApplyReq) {
        this.goodAdminApplyReq = goodAdminApplyReq;
    }

    public void setGoodSearchGetStationeryTypeReq(GoodSearchGetStationeryTypeReq goodSearchGetStationeryTypeReq) {
        this.goodSearchGetStationeryTypeReq = goodSearchGetStationeryTypeReq;
    }

    public void setGoodNumCountReq(GoodNumCountReq goodNumCountReq) {
        this.goodNumCountReq = goodNumCountReq;
    }

    public void setGoodNumAddReq(GoodNumAddReq goodNumAddReq) {
        this.goodNumAddReq = goodNumAddReq;
    }

    public void setGoodNumGetByIdReq(GoodNumGetByIdReq goodNumGetByIdReq) {
        this.goodNumGetByIdReq = goodNumGetByIdReq;
    }

    public void setGoodNumUserListReq(GoodNumUserListReq goodNumUserListReq) {
        this.goodNumUserListReq = goodNumUserListReq;
    }

    public void setGoodNumAdminListReq(GoodNumAdminListReq goodNumAdminListReq) {
        this.goodNumAdminListReq = goodNumAdminListReq;
    }

    public void setGoodNumDoReq(GoodNumDoReq goodNumDoReq) {
        this.goodNumDoReq = goodNumDoReq;
    }

    public void setCheckGetTypeReq(CheckGetTypeReq checkGetTypeReq) {
        this.checkGetTypeReq = checkGetTypeReq;
    }

    public void setCheckGetIllTypeReq(CheckGetIllTypeReq checkGetIllTypeReq) {
        this.checkGetIllTypeReq = checkGetIllTypeReq;
    }

    public void setCheckGetClassReq(CheckGetClassReq checkGetClassReq) {
        this.checkGetClassReq = checkGetClassReq;
    }

    public void setCheckGetStuReq(CheckGetStuReq checkGetStuReq) {
        this.checkGetStuReq = checkGetStuReq;
    }

    public void setCheckGetStuToIllReq(CheckGetStuToIllReq checkGetStuToIllReq) {
        this.checkGetStuToIllReq = checkGetStuToIllReq;
    }

    public void setCheckStuParentDetailReq(CheckStuParentDetailReq checkStuParentDetailReq) {
        this.checkStuParentDetailReq = checkStuParentDetailReq;
    }

    public void setCheckStuChildDetailReq(CheckStuChildDetailReq checkStuChildDetailReq) {
        this.checkStuChildDetailReq = checkStuChildDetailReq;
    }

    public void setCheckSubimtYesReq(CheckSubimtYesReq checkSubimtYesReq) {
        this.checkSubimtYesReq = checkSubimtYesReq;
    }

    public void setCheckAddChildReq(CheckAddChildReq checkAddChildReq) {
        this.checkAddChildReq = checkAddChildReq;
    }

    public void setCheckAddParentReq(CheckAddParentReq checkAddParentReq) {
        this.checkAddParentReq = checkAddParentReq;
    }

    public void setCheckStuDelChildReq(CheckStuDelChildReq checkStuDelChildReq) {
        this.checkStuDelChildReq = checkStuDelChildReq;
    }

    public void setCheckStuDelParentReq(CheckStuDelParentReq checkStuDelParentReq) {
        this.checkStuDelParentReq = checkStuDelParentReq;
    }

    public void setCheckTjListReq(CheckTjListReq checkTjListReq) {
        this.checkTjListReq = checkTjListReq;
    }

    public void setSealGetSealTypeReq(SealGetSealTypeReq sealGetSealTypeReq) {
        this.sealGetSealTypeReq = sealGetSealTypeReq;
    }

    public void setSealUserListReq(SealUserListReq sealUserListReq) {
        this.sealUserListReq = sealUserListReq;
    }

    public void setSealAdminListReq(SealAdminListReq sealAdminListReq) {
        this.sealAdminListReq = sealAdminListReq;
    }

    public void setSealMasterCountReq(SealMasterCountReq sealMasterCountReq) {
        this.sealMasterCountReq = sealMasterCountReq;
    }

    public void setSealApplyMasterUserReq(SealApplyMasterUserReq sealApplyMasterUserReq) {
        this.sealApplyMasterUserReq = sealApplyMasterUserReq;
    }

    public void setSealApplyAddReq(SealApplyAddReq sealApplyAddReq) {
        this.sealApplyAddReq = sealApplyAddReq;
    }

    public void setSealByIdItemReq(SealByIdItemReq sealByIdItemReq) {
        this.sealByIdItemReq = sealByIdItemReq;
    }

    public void setSealAllDetailReq(SealAllDetailReq sealAllDetailReq) {
        this.sealAllDetailReq = sealAllDetailReq;
    }

    public void setSealSetDoReq(SealSetDoReq sealSetDoReq) {
        this.sealSetDoReq = sealSetDoReq;
    }

    public void setDelayAdminGetTodayListReq(DelayAdminGetTodayListReq delayAdminGetTodayListReq) {
        this.delayAdminGetTodayListReq = delayAdminGetTodayListReq;
    }

    public void setDelayAdminGetClassListReq(DelayAdminGetClassListReq delayAdminGetClassListReq) {
        this.delayAdminGetClassListReq = delayAdminGetClassListReq;
    }

    public void setDelayAdminGetToClassEventDetailReq(DelayAdminGetToClassEventDetailReq delayAdminGetToClassEventDetailReq) {
        this.delayAdminGetToClassEventDetailReq = delayAdminGetToClassEventDetailReq;
    }

    public void setDelayAdminClassSetReq(DelayAdminClassSetReq delayAdminClassSetReq) {
        this.delayAdminClassSetReq = delayAdminClassSetReq;
    }

    public void setDelayGetTypeReq(DelayGetTypeReq delayGetTypeReq) {
        this.delayGetTypeReq = delayGetTypeReq;
    }

    public void setDelayGetItemDetailReq(DelayGetItemDetailReq delayGetItemDetailReq) {
        this.delayGetItemDetailReq = delayGetItemDetailReq;
    }

    public void setDelayGetClassStuListReq(DelayGetClassStuListReq delayGetClassStuListReq) {
        this.delayGetClassStuListReq = delayGetClassStuListReq;
    }

    public void setDelayDelStuItemReq(DelayDelStuItemReq delayDelStuItemReq) {
        this.delayDelStuItemReq = delayDelStuItemReq;
    }

    public void setDelayGetClassListTeaReq(DelayGetClassListTeaReq delayGetClassListTeaReq) {
        this.delayGetClassListTeaReq = delayGetClassListTeaReq;
    }

    public void setDelayGetClassListReplaceReq(DelayGetClassListReplaceReq delayGetClassListReplaceReq) {
        this.delayGetClassListReplaceReq = delayGetClassListReplaceReq;
    }

    public void setDelayGetCopyListReq(DelayGetCopyListReq delayGetCopyListReq) {
        this.delayGetCopyListReq = delayGetCopyListReq;
    }

    public void setDelayCopySetReq(DelayCopySetReq delayCopySetReq) {
        this.delayCopySetReq = delayCopySetReq;
    }

    public void setDelayTeaAddReq(DelayTeaAddReq delayTeaAddReq) {
        this.delayTeaAddReq = delayTeaAddReq;
    }

    public void setProcessGetTypeReq(ProcessGetTypeReq processGetTypeReq) {
        this.processGetTypeReq = processGetTypeReq;
    }

    public void setProcessGetUserListReq(ProcessGetUserListReq processGetUserListReq) {
        this.processGetUserListReq = processGetUserListReq;
    }

    public void setProcessGetDetailReq(ProcessGetDetailReq processGetDetailReq) {
        this.processGetDetailReq = processGetDetailReq;
    }

    public void setProcessDelDetailReq(ProcessDelDetailReq processDelDetailReq) {
        this.processDelDetailReq = processDelDetailReq;
    }

    public void setProcessAdminSetClassReq(ProcessAdminSetClassReq processAdminSetClassReq) {
        this.processAdminSetClassReq = processAdminSetClassReq;
    }

    public void setProcessAdminSetStateReq(ProcessAdminSetStateReq processAdminSetStateReq) {
        this.processAdminSetStateReq = processAdminSetStateReq;
    }
}
