package com.atakmap.android.plugin.rain.pulse.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.atakmap.android.plugin.rain.pulse.cot.PulseCotDetail;
import com.atakmap.android.plugin.rain.pulse.track.sim.PulseHelivacSimulator;
import com.atakmap.coremap.cot.event.CotDetail;

public class CasevacDetailsInputs implements Parcelable {

    public static final String COT_DETAIL_PULSE_CASEVAC_DETAILS = "casevac_details";
    private static final String COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_STATUS = "status";
    private static final String COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_CALLSIGN = "inbound_callsign";
    private static final String COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_PLATFORM = "inbound_platform";
    private static final String COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_ETA = "eta";
    private static final String COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_REMARKS = "remarks";
    private static final String COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_INBOUND_UID = "inbound_uid";

    String _status;
    String _casevacCallsign;
    String _casevacPlatform;
    String _eta;
    String _remarks;
    String _casevacInboundUid = PulseHelivacSimulator.SIM_UID_DEFAULT;//used for simulation

    public CasevacDetailsInputs(){ }

    public CasevacDetailsInputs(String status, String casevacCallsign, String casevacPlatform, String eta, String remarks) {
        _status = status;
        _casevacCallsign = casevacCallsign;
        _casevacPlatform = casevacPlatform;
        _eta = eta;
        _remarks = remarks;
    }

    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        _status = status;
    }

    public String getCasevacCallsign() {
        return _casevacCallsign;
    }

    public void setCasevacCallsign(String _casevacCallsign) {
        this._casevacCallsign = _casevacCallsign;
    }

    public String getCasevacPlatform() {
        return _casevacPlatform;
    }

    public void setCasevacPlatform(String casevacPlatform) {
        this._casevacPlatform = casevacPlatform;
    }

    public String getEta() {
        return _eta;
    }

    public void setEta(String eta) {
        _eta = eta;
    }

    public void setRemarks(String remarks) {
        _remarks = remarks;
    }

    public String getInBoundUid(){
        return _casevacInboundUid;
    }

    protected CasevacDetailsInputs(Parcel in) {
        _status = in.readString();
        _casevacCallsign = in.readString();
        _casevacPlatform = in.readString();
        _eta = in.readString();
        _remarks = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_status);
        dest.writeString(_casevacCallsign);
        dest.writeString(_casevacPlatform);
        dest.writeString(_eta);
        dest.writeString(_remarks);
    }

    @SuppressWarnings("unused")
    public static final Creator<CasevacDetailsInputs> CREATOR = new Creator<CasevacDetailsInputs>() {
        @Override
        public CasevacDetailsInputs createFromParcel(Parcel in) {
            return new CasevacDetailsInputs(in);
        }

        @Override
        public CasevacDetailsInputs[] newArray(int size) {
            return new CasevacDetailsInputs[size];
        }
    };

    public CotDetail toCotDetail(){
        CotDetail detail = new CotDetail(COT_DETAIL_PULSE_CASEVAC_DETAILS);
        detail.setAttribute(COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_STATUS, _status);
        detail.setAttribute(COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_CALLSIGN, _casevacCallsign);
        detail.setAttribute(COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_PLATFORM, _casevacPlatform);
        detail.setAttribute(COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_ETA, _eta);
        detail.setAttribute(COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_REMARKS, _remarks);
        detail.setAttribute(COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_INBOUND_UID, _casevacInboundUid);

        return detail;
    }

    public CasevacDetailsInputs(CotDetail detail)
    {
        if(!detail.getElementName().equals(COT_DETAIL_PULSE_CASEVAC_DETAILS))return;

        _status = PulseCotDetail.getAttribute(COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_STATUS, detail);
        Log.d("TAG", "CasevacDetailsInputs: " + _status);
        _casevacCallsign = PulseCotDetail.getAttribute(COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_CALLSIGN, detail);
        _casevacPlatform = PulseCotDetail.getAttribute(COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_PLATFORM, detail);
        _eta = PulseCotDetail.getAttribute(COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_ETA, detail);
        _remarks = PulseCotDetail.getAttribute(COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_REMARKS, detail);
        _casevacInboundUid = PulseCotDetail.getAttribute(COT_DETAIL_ATTR_PULSE_CASEVAC_DETAILS_INBOUND_UID, detail);
    }

    public static CasevacDetailsInputs parsePulseDetail(CotDetail pulseRequestDetail) {
        CotDetail statusDetail = pulseRequestDetail.getFirstChildByName(0, COT_DETAIL_PULSE_CASEVAC_DETAILS);
        if(statusDetail == null)return null;
        return new CasevacDetailsInputs(statusDetail);
    }

}
