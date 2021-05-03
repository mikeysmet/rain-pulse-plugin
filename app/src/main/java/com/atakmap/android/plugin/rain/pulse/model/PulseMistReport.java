package com.atakmap.android.plugin.rain.pulse.model;

import com.atakmap.coremap.cot.event.CotDetail;
import com.atakmap.coremap.cot.event.CotEvent;

public class PulseMistReport {

    private String _title;//free text
    private String _zapNumber;//free text
    private String _mechanismOfInjury;//follow core options?
    private String _injurySustained;//follow core options?
    private String _symptoms; //follow core options?
    private String _treatment;//free text

    public PulseMistReport(){
        //initialize with simulated values for now
        _title = "Pulse MIST";
        _zapNumber = "6";
        _injurySustained = "Blast";
        //Bleeding: Massive&#10;Airway: Has Airway&#10;Pulse Radial: No Radial
        _symptoms = "Bleeding: Massive";
        _treatment = "band-aid";//just free text
    }
    public PulseMistReport(String title, String zapNumber, String _mechanismOfInjury, String injurySustained ){
        //initialize with simulated values for now
        _title = "Pulse MIST " + zapNumber;
        _zapNumber = zapNumber;
        _injurySustained = injurySustained;
        //Bleeding: Massive&#10;Airway: Has Airway&#10;Pulse Radial: No Radial
        _symptoms = "Bleeding: Massive";
        _treatment = "band-aid";//just free text
    }

    /**
     * <_medevac_
     *      medline_remarks='' security='0' equipment_none='true' freq='425.225'
     *      terrain_none='true' hlz_marking='3' casevac='false'
     *      zone_prot_selection='0' title='joker'>
     *      <zMistsMap>
     *          <zMist title='ZMIST1'
     *              z='69' m='Crush Fall/MVA  Burn &gt; 20% Blunt Trauma Chemical'
     *              i='Hematoma Burn'
     *              s='Bleeding: Massive&#10;Airway: Has Airway&#10;
     *                  Pulse Radial: No Radial&#10;Pulse Strength: Strong/Steady&#10;
     *                  Skin: Cold/Clammy&#10;Pupils: Dilated&#10;Breathing: Normal'
     *              t='treatment'   />
     *      </zMistsMap>
     *  </_medevac_>
    * */


    public CotDetail toDetail(){
        CotDetail mistDetail = new CotDetail("zMist");
        mistDetail.setAttribute("title", _title);
        mistDetail.setAttribute("z", _zapNumber);
        mistDetail.setAttribute("m", _mechanismOfInjury);
        mistDetail.setAttribute("i", _injurySustained);
        mistDetail.setAttribute("s", _symptoms);
        mistDetail.setAttribute("t", _treatment);
        return mistDetail;
    }

    public void addMistToEvent(CotEvent coreCasevacEvent){
        CotDetail mistDetail = toDetail();
        CotDetail detail = coreCasevacEvent.getDetail();
        //first find medevac
        CotDetail medevacDetail = detail.getFirstChildByName(0, "_medevac_");
        if(medevacDetail == null)return;
        //now see if we have a zListsMap
        CotDetail zMistMapDetail = medevacDetail.getFirstChildByName(0,"zMistsMap");

        if(zMistMapDetail == null){
            //if not, add one
            zMistMapDetail = new CotDetail("zMistsMap");
            detail.addChild(zMistMapDetail);
        }
        //now add our report.
        zMistMapDetail.addChild(mistDetail);
    }

}
