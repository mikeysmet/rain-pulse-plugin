package com.atakmap.android.plugin.rain.pulse.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.atakmap.android.gui.PluginSpinner;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugin.rain.pulse.R;
import com.atakmap.android.plugin.rain.pulse.casevac.PulseCasevacProcessor;
import com.atakmap.android.plugin.rain.pulse.model.PulseMistReport;
import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;
import com.atakmap.android.plugin.rain.pulse.model.TreatmentInputs;
import com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs;
import com.atakmap.android.plugin.rain.pulse.ui.adapter.GridViewAdapter;


import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.atakmap.android.maps.MapView._mapView;
import static com.atakmap.android.maps.MapView.getMapView;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_ANALGESIC_LIST;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_ANTIBIOTIC_LIST;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_BLOOD_PROD_LIST;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_BODY_PART;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_BODY_PART_LOC_X;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_BODY_PART_LOC_Y;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_BODY_PART_VIS;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_CONFIRMATION_IV_DONUT_X;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_CONFIRMATION_IV_DONUT_Y;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_FLUID_LIST;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_INJURY_IV_DONUT_POS;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_INJURY_IV_DONUT_X;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_INJURY_IV_DONUT_Y;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_IV_CONFIRM_CHECK_MARK_VIS;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_TCCC_VIS;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_TQ_CHECKED;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_TQ_TIME;
import static com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs.KEY_CASEVAC_TREATMENTS_CHECKBOXES;

public class TraumaDialogView extends LinearLayout {

    //TODO -  this file needs to be broken up into logical components.
    //TODO -  the layout_id's and resource IDs need consistent naming
    //TODO - this is a mess
    //TODO - the components need to be styled in the xml.  THe layout has too many repeated properties
    //     -   look at the checkboxes, for example.

    private Context _pluginContext;
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getMapView().getContext());
    private View _view;
    private TreatmentInputs _treatmentInputs = new TreatmentInputs();
    PluginSpinner spinnerFluid, spinnerBlood1,
            spinnerAnalgesicType1, spinnerDoseUnit, spinnerAntibiotics1,
            spinnerFluidRoute, spinnerBloodRoute, spinnerAnalgesicRoute, spinnerAntibioticRoute;

    ScrollView scrollViewLayoutTrauma;
    String fluidSelected, analgesicTypeSelected, antibioticsTypeSelected, bloodProductSelected, doseUnitSelected, routeSelectedString;
    TextView textViewInjuryTitle;
    LinearLayout llTreatments, _linearLayoutTreatmentTable, linearLayoutKeypad;
    FrameLayout frameLayoutTcccMan;
    ImageView iv_donut, imageViewConfirmTraumaGreen, ivAddTrauma, ivSmallTcccman, imageViewFlipTccc;
    GridView gridView;
    GridLayout gridViewKeyPad;
    TextView textViewBodyPart;
    TextView textViewDose;
    TextView textViewDoseMeasurement;
    boolean isTcccManVis;

    EditText _editTextOtherInjury;
    EditText editTextFluidTime;
    EditText editTextBloodProdTime;
    EditText editTextAnalgesicTime;
    EditText editTextAntibioticTime;
    EditText editTextHypoType;

    List<CheckBox> _mechOfInjuryCheckBoxes;
    List<CheckBox> _treatmentsACheckBoxes;
    List<CheckBox> _treatmentsBCheckBoxes;
    List<CheckBox> _treatmentsCCheckBoxes;
    List<CheckBox> _treatmentsOtherCheckBoxes;
    List<CheckBox> checkBoxesTq;

    CheckBox _checkBoxBlast;
    CheckBox _checkBoxBlunt;
    CheckBox _checkBoxBullet;
    CheckBox _checkBoxBurn;

    CheckBox _checkBoxCold;
    CheckBox _checkBoxHeat;
    CheckBox _checkBoxCrush;
    CheckBox _checkBoxFall;

    CheckBox _checkBoxFragS;
    CheckBox _checkBoxFragM;
    CheckBox _checkBoxKnife;
    CheckBox _checkBoxRad;

    CheckBox _checkBoxNuc;
    CheckBox _checkBoxChem;
    CheckBox _checkBoxSmoke;
    CheckBox _checkBoxSting;

    CheckBox _checkBoxTqLArm;
    CheckBox _checkBoxTqRArm;
    CheckBox _checkBoxTqLftLeg;
    CheckBox _checkBoxTqRtLeg;
    CheckBox _checkBoxPressure;
    CheckBox _checkBoxElevation;

    CheckBox _checkBoxExtremity;
    CheckBox _checkBoxJunc;
    CheckBox _checkBoxTrnc;

    CheckBox _checkBoxDressing;
    CheckBox _checkBoxHemo;
    CheckBox _checkBoxDirectPressure;
    CheckBox _checkBoxOther;

    CheckBox _checkBoxIntact;
    CheckBox _checkBoxNPA;
    CheckBox _checkBoxCRIC;
    CheckBox _checkBoxEtTube;
    CheckBox _checkBoxSGA;

    CheckBox _checkBoxO2;
    CheckBox _checkBoxNeedleD;
    CheckBox _checkBoxChestTube;
    CheckBox _checkBoxChestSeal;

    CheckBox _checkBoxOPillPack;
    CheckBox _checkBoxEyeShield;
    CheckBox _checkBoxRight;
    CheckBox _checkBoxLeft;
    CheckBox _checkBoxSplint;
    CheckBox _checkBoxHypo;

    List<Button> buttonsDoseList = new ArrayList<>();
    List<PluginSpinner> spinnerRouteList = new ArrayList<>();
    Button buttonFluidVol, buttonBloodProductVol, buttonAnalgesicDose, buttonAntibioticDose;
    Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button0, buttonDel, buttonDone;
    Button buttonResetPrefs;

    List<String> _injuryLocationList;
    List<String> _mechOfInjuryList;
    List<String> _tqTreatmentsList;
    List<String> _tqTimeStampList;
    List<String> _treatmentsCList;
    List<String> _treatmentsAList;
    List<String> _treatmentsBList;
    List<String> _treatmentsOtherList;
    List<String> _treatmentsTableFluidList = new ArrayList<>();
    List<String> _treatmentsTableBloodList = new ArrayList<>();
    List<String> _treatmentsTableAnalgesicList = new ArrayList<>();
    List<String> _treatmentsTableAntibioticList = new ArrayList<>();

    TeamMemberInputs _teamMemberInputs;
    TreatmentInputs _casualtyTreatmentInputs;

    int[] gridBox = new int[108];

    public TraumaDialogView(Context context) {
        super(context);

    }


    public TraumaDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TraumaDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setup(Context pluginContext, TeamMemberInputs casualtyInputs) {
        _pluginContext = pluginContext;
        _teamMemberInputs = casualtyInputs;

        _view = LayoutInflater.from(pluginContext).inflate(R.layout.layout_trauma, this);

        findViews(_view);
    }

    private void findViews(View view) {
        scrollViewLayoutTrauma = _view.findViewById(R.id.ll_trauma_definition);

        llTreatments = _view.findViewById(R.id.ll_treatments);
        _linearLayoutTreatmentTable = _view.findViewById(R.id.ll_treatment_table);
        linearLayoutKeypad = _view.findViewById(R.id.ll_keypad);
        frameLayoutTcccMan = _view.findViewById(R.id.fl_trauma_tccc_man);

        //TextViews
        textViewBodyPart = _view.findViewById(R.id.tv_body_part);
        textViewInjuryTitle = _view.findViewById(R.id.tv_injury_desc);
        textViewDose = _view.findViewById(R.id.tv_dose_num);
        textViewDoseMeasurement = _view.findViewById(R.id.tv_dose_msr);

        //Buttons

        //GridView
        gridView = _view.findViewById(R.id.gv_tccc);
        gridViewKeyPad = _view.findViewById(R.id.gl_keypad);

        //imageViews
        iv_donut = _view.findViewById(R.id.iv_trauma_donut);
        imageViewConfirmTraumaGreen = _view.findViewById(R.id.iv_trauma_donut2);
        ivAddTrauma = _view.findViewById(R.id.iv_trauma_add);
        ivSmallTcccman = _view.findViewById(R.id.iv_flip_small_tccc);
        imageViewFlipTccc = view.findViewById(R.id.ic_flipTccc);

        spinnerDoseUnit = _view.findViewById(R.id.spinner_dose_measurement);
        spinnerFluid = _view.findViewById(R.id.spinner_fluid_1);
        spinnerBlood1 = _view.findViewById(R.id.spinner_blood_product_1);
        spinnerAnalgesicType1 = _view.findViewById(R.id.spinner_analgesic_one);
        spinnerAntibiotics1 = _view.findViewById(R.id.spinner_antibiotics_one);

        //route Spinners
        spinnerFluidRoute = _view.findViewById(R.id.spinner_fluid_route);
        spinnerBloodRoute = _view.findViewById(R.id.spinner_blood_product_route);
        spinnerAnalgesicRoute = _view.findViewById(R.id.spinner_analgesic_route);
        spinnerAntibioticRoute = _view.findViewById(R.id.spinner_antibiotic_route);

        spinnerRouteList.add(spinnerFluidRoute);
        spinnerRouteList.add(spinnerBloodRoute);
        spinnerRouteList.add(spinnerAnalgesicRoute);
        spinnerRouteList.add(spinnerAntibioticRoute);


        //checkboxes Mech of injury
        _checkBoxBlast = _view.findViewById(R.id.cb_btn_blast);
        _checkBoxBlunt = _view.findViewById(R.id.cb_btn_blunt);
        _checkBoxBullet = _view.findViewById(R.id.cb_btn_bullet);
        _checkBoxBurn = _view.findViewById(R.id.cb_btn_burn);

        _checkBoxCold = _view.findViewById(R.id.cb_btn_cold);
        _checkBoxHeat = _view.findViewById(R.id.cb_btn_heat);
        _checkBoxCrush = _view.findViewById(R.id.cb_btn_crush);
        _checkBoxFall = _view.findViewById(R.id.cb_btn_fall);

        _checkBoxFragS = _view.findViewById(R.id.cb_btn_frag_s);
        _checkBoxFragM = _view.findViewById(R.id.cb_btn_frag_m);
        _checkBoxKnife = _view.findViewById(R.id.cb_btn_knife);
        _checkBoxRad = _view.findViewById(R.id.cb_btn_rad);

        _checkBoxNuc = _view.findViewById(R.id.cb_btn_nuc);
        _checkBoxChem = _view.findViewById(R.id.cb_btn_chem);
        _checkBoxSmoke = _view.findViewById(R.id.cb_btn_smoke);
        _checkBoxSting = _view.findViewById(R.id.cb_btn_sting);

        //other option
        _editTextOtherInjury = _view.findViewById(R.id.et_mech_injury_other);
        editTextFluidTime = _view.findViewById(R.id.et_fluid_time);
        editTextBloodProdTime = _view.findViewById(R.id.et_blood_time);
        editTextAnalgesicTime = _view.findViewById(R.id.et_analgesic_time);
        editTextAntibioticTime = _view.findViewById(R.id.et_antibiotic_time);

        editTextHypoType = _view.findViewById(R.id.et_type_hypo_type);
        editTextHypoType.getText().clear();

        //Checkboxes Tq Treatments
        _checkBoxTqLArm = _view.findViewById(R.id.cb_tq_L_arm);
        _checkBoxTqRArm = _view.findViewById(R.id.cb_tq_R_arm);
        _checkBoxTqLftLeg = _view.findViewById(R.id.cb_tq_L_leg);
        _checkBoxTqRtLeg = _view.findViewById(R.id.cb_tq_R_leg);
        _checkBoxPressure = _view.findViewById(R.id.cb_btn_pressure);
        _checkBoxElevation = _view.findViewById(R.id.cb_btn_elevation);

        //Checkboxes treatments
        _checkBoxExtremity = _view.findViewById(R.id.cb_btn_extremity);
        _checkBoxJunc = _view.findViewById(R.id.cb_btn_junc);
        _checkBoxTrnc = _view.findViewById(R.id.cb_btn_trunc);
        _checkBoxDressing = _view.findViewById(R.id.cb_btn_dressing);
        _checkBoxHemo = _view.findViewById(R.id.cb_btn_hemo);
        _checkBoxDirectPressure = _view.findViewById(R.id.cb_btn_dir_pressure);
        _checkBoxOther = _view.findViewById(R.id.cb_btn_other2);

        _checkBoxIntact = _view.findViewById(R.id.cb_btn_intact);
        _checkBoxNPA = _view.findViewById(R.id.cb_btn_npa);
        _checkBoxCRIC = _view.findViewById(R.id.cb_btn_cric);
        _checkBoxEtTube = _view.findViewById(R.id.cb_btn_et_tube);
        _checkBoxSGA = _view.findViewById(R.id.cb_btn_sga);

        _checkBoxO2 = _view.findViewById(R.id.cb_btn_o2);
        _checkBoxNeedleD = _view.findViewById(R.id.cb_btn_needle_d);
        _checkBoxChestTube = _view.findViewById(R.id.cb_btn_chest_tube);
        _checkBoxChestSeal = _view.findViewById(R.id.cb_btn_chest_seal);

        _checkBoxOPillPack = _view.findViewById(R.id.cb_btn_pill_pack);
        _checkBoxEyeShield = _view.findViewById(R.id.cb_btn_eye_shield);
        _checkBoxRight = _view.findViewById(R.id.cb_btn_right);
        _checkBoxLeft = _view.findViewById(R.id.cb_btn_left);
        _checkBoxSplint = _view.findViewById(R.id.cb_btn_splint);
        _checkBoxHypo = _view.findViewById(R.id.cb_btn_hypothermia);

        //Buttons
        buttonResetPrefs = _view.findViewById(R.id.button_reset_prefs);
        buttonFluidVol = _view.findViewById(R.id.btn_fluid_vol1);
        buttonBloodProductVol = _view.findViewById(R.id.btn_blood_prod_vol);
        buttonAnalgesicDose = _view.findViewById(R.id.btn_dose_analgesic);
        buttonAntibioticDose = _view.findViewById(R.id.btn_dose_antibiotic);


        buttonsDoseList.add(buttonFluidVol);
        buttonsDoseList.add(buttonBloodProductVol);
        buttonsDoseList.add(buttonAnalgesicDose);
        buttonsDoseList.add(buttonAntibioticDose);


        button0 = _view.findViewById(R.id.btn_0);
        button1 = _view.findViewById(R.id.btn_1);
        button2 = _view.findViewById(R.id.btn_2);
        button3 = _view.findViewById(R.id.btn_3);
        button4 = _view.findViewById(R.id.btn_4);
        button5 = _view.findViewById(R.id.btn_5);
        button6 = _view.findViewById(R.id.btn_6);
        button7 = _view.findViewById(R.id.btn_7);
        button8 = _view.findViewById(R.id.btn_8);
        button9 = _view.findViewById(R.id.btn_9);
        buttonDel = _view.findViewById(R.id.btn_del);
        buttonDone = _view.findViewById(R.id.btn_dose_done);
        setPointOfInjury();
        getMechanismOfInjury();
        getTQTreatments();
        getTreatmentsC();
        getTreatmentsA();
        getTreatmentsB();
        getTreatmentsOther();
        restoreDefaultSelectionsOnClick();
        flipTcccViewOnClick();
        hideTCCCManOnClick();
    }


    private void getTcccManGridAdapter() {
        GridViewAdapter gridviewAdapter = new GridViewAdapter(_pluginContext, gridBox, gridView);
        gridView.setAdapter(gridviewAdapter);
    }

    private void getDoseKeyPad(Button buttonClicked) {
        button1.requestFocus();
        textViewDoseMeasurement.setText("Unit");
        textViewDose.setText("");
        linearLayoutKeypad.setVisibility(VISIBLE);
        List<Button> buttonList = new ArrayList<>();
        buttonList.add(button1);
        buttonList.add(button2);
        buttonList.add(button3);
        buttonList.add(button4);
        buttonList.add(button5);
        buttonList.add(button6);
        buttonList.add(button7);
        buttonList.add(button8);
        buttonList.add(button9);
        buttonList.add(button0);
        buttonList.add(buttonDel);
        buttonList.add(buttonDone);
        getDoseUnitSpinner();

        for (int i = 0; i < buttonList.size(); i++) {
            int position = i;
            buttonList.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    gatherDoseData(buttonList.get(position), buttonClicked);
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void gatherDoseData(Button numberClicked, Button buttonDoseClicked) {
        String clickedButton = String.valueOf(numberClicked.getText());
        switch (clickedButton) {
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
                textViewDose.append(clickedButton);

                break;

            case "Done":
                linearLayoutKeypad.setVisibility(GONE);
                _linearLayoutTreatmentTable.setVisibility(VISIBLE);
                buttonDoseClicked.setText(textViewDose.getText() + " " + doseUnitSelected);
                setTimeStamp(buttonDoseClicked);

                Log.d("TAG", "gatherDoseData: " + buttonDoseClicked.getId());
                handleTableTreatments(buttonDoseClicked.getId(), buttonDoseClicked);

                break;

            case "Delete":
                String display = textViewDose.getText().toString();

                if (!TextUtils.isEmpty(display)) {
                    display = display.substring(0, display.length() - 1);
                    textViewDose.setText(display);
                }
                break;
            default:
                break;
        }

    }

    private void handleTableTreatments(int clickedButton, Button doseButton) {

        switch (clickedButton) {
            //TODO - why are these hard coded?
            case 2131296366:
                _treatmentsTableFluidList.clear();
                if (!fluidSelected.isEmpty()) {
                    _treatmentsTableFluidList.add(fluidSelected);
                } else {
                    _treatmentsTableFluidList.add(String.valueOf(spinnerFluid.getSelectedItem()));

                }
                _treatmentsTableFluidList.add(String.valueOf(doseButton.getText()));
                _treatmentsTableFluidList.add(spinnerFluidRoute.getSelectedItem().toString());
                _treatmentsTableFluidList.add(String.valueOf(editTextFluidTime.getText()));
                break;
            case 2131296361:
                _treatmentsTableBloodList.clear();
                if (!bloodProductSelected.isEmpty()) {
                    _treatmentsTableBloodList.add(bloodProductSelected);
                } else {
                    _treatmentsTableBloodList.add(String.valueOf(spinnerBlood1.getSelectedItem()));

                }
                _treatmentsTableBloodList.add(String.valueOf(doseButton.getText()));
                _treatmentsTableBloodList.add(spinnerFluidRoute.getSelectedItem().toString());
                _treatmentsTableBloodList.add(String.valueOf(editTextBloodProdTime.getText()));
                break;

            case 2131296363:
                _treatmentsTableAnalgesicList.clear();
                if (!analgesicTypeSelected.isEmpty()) {
                    _treatmentsTableAnalgesicList.add(analgesicTypeSelected);
                } else {
                    _treatmentsTableAnalgesicList.add(String.valueOf(spinnerAnalgesicType1.getSelectedItem()));

                }
                _treatmentsTableAnalgesicList.add(String.valueOf(doseButton.getText()));
                _treatmentsTableAnalgesicList.add(spinnerAnalgesicRoute.getSelectedItem().toString());
                _treatmentsTableAnalgesicList.add(String.valueOf(editTextAnalgesicTime.getText()));
                break;

            case 2131296364:
                _treatmentsTableAntibioticList.clear();
                if (!antibioticsTypeSelected.isEmpty()) {
                    _treatmentsTableAntibioticList.add(antibioticsTypeSelected);
                } else {
                    _treatmentsTableAntibioticList.add(String.valueOf(spinnerAntibiotics1.getSelectedItem()));

                }
                _treatmentsTableAntibioticList.add(String.valueOf(doseButton.getText()));
                _treatmentsTableAntibioticList.add(spinnerAntibioticRoute.getSelectedItem().toString());
                _treatmentsTableAntibioticList.add(String.valueOf(editTextAntibioticTime.getText()));
                break;

        }

        Log.d("TAG", "handleTableTreatments: " + frameLayoutTcccMan.getVisibility());
        PulsePrefs.storeTreatmentTablePreferences(
                _treatmentsTableFluidList, _treatmentsTableBloodList,
                _treatmentsTableAnalgesicList, _treatmentsTableAntibioticList,
                true);
    }


    private String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        String format = simpleDateFormat.format(new Date());
        return format;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ResourceType", "UseCompatLoadingForDrawables"})
    private void getMechanismOfInjury() {

        _mechOfInjuryList = new ArrayList<>();
        //make these accessible to whole class
        _mechOfInjuryCheckBoxes = new ArrayList<>();
        _mechOfInjuryCheckBoxes.add(_checkBoxBlast);
        _mechOfInjuryCheckBoxes.add(_checkBoxBlunt);
        _mechOfInjuryCheckBoxes.add(_checkBoxBullet);
        _mechOfInjuryCheckBoxes.add(_checkBoxBurn);
        _mechOfInjuryCheckBoxes.add(_checkBoxCold);
        _mechOfInjuryCheckBoxes.add(_checkBoxHeat);
        _mechOfInjuryCheckBoxes.add(_checkBoxCrush);
        _mechOfInjuryCheckBoxes.add(_checkBoxFall);
        _mechOfInjuryCheckBoxes.add(_checkBoxFragS);
        _mechOfInjuryCheckBoxes.add(_checkBoxFragM);
        _mechOfInjuryCheckBoxes.add(_checkBoxKnife);
        _mechOfInjuryCheckBoxes.add(_checkBoxRad);
        _mechOfInjuryCheckBoxes.add(_checkBoxNuc);
        _mechOfInjuryCheckBoxes.add(_checkBoxChem);
        _mechOfInjuryCheckBoxes.add(_checkBoxSmoke);
        _mechOfInjuryCheckBoxes.add(_checkBoxSting);
        getInjuryOtherEditText();
        getTreatmentPrefs(_mechOfInjuryCheckBoxes, _mechOfInjuryList);

        for (int i = 0; i < _mechOfInjuryCheckBoxes.size(); i++) {
            CheckBox _checkBoxSelected = _mechOfInjuryCheckBoxes.get(i);
            _checkBoxSelected.setOnCheckedChangeListener((compoundButton, b) -> {
                if (_checkBoxSelected.isChecked()) {
                    _checkBoxSelected.setBackgroundColor(_pluginContext.getColor(R.color.blue));
                    _mechOfInjuryList.add(String.valueOf(_checkBoxSelected.getText()));

                } else {
                    _checkBoxSelected.setBackground(_pluginContext.getDrawable(R.drawable.table_borders));
                    _mechOfInjuryList.remove(_checkBoxSelected.getText());
                }
                PulsePrefs.storeTreatmentCheckBoxPrefs(_mechOfInjuryCheckBoxes, null);
            });
        }
    }

    private void getTQTreatments() {

        _tqTreatmentsList = new ArrayList<>();
        checkBoxesTq = new ArrayList<>();
        checkBoxesTq.add(_checkBoxTqLArm);
        checkBoxesTq.add(_checkBoxTqRArm);
        checkBoxesTq.add(_checkBoxTqLftLeg);
        checkBoxesTq.add(_checkBoxTqRtLeg);
        checkBoxesTq.add(_checkBoxPressure);
        checkBoxesTq.add(_checkBoxElevation);

        getTQPrefs(checkBoxesTq, _tqTreatmentsList, _tqTimeStampList);


        for (int i = 0; i < checkBoxesTq.size(); i++) {
            CheckBox _checkBoxSelected = checkBoxesTq.get(i);
            _checkBoxSelected.setOnCheckedChangeListener((compoundButton, b) -> {
                if (_checkBoxSelected.isChecked()) {
                    _checkBoxSelected.setBackgroundColor(_pluginContext.getColor(R.color.blue));
                    _checkBoxSelected.append("\n" + getTime());
                    _tqTreatmentsList.add(String.valueOf(_checkBoxSelected.getText()));

                } else {
                    _checkBoxSelected.setBackground(_pluginContext.getDrawable(R.drawable.table_borders));
                    String result = _checkBoxSelected.getText().toString().split("\n")[0];
                    _tqTreatmentsList.remove(_checkBoxSelected.getText());
                    _checkBoxSelected.setText(result);
                }

                PulsePrefs.storeTQCheckBoxPrefs(checkBoxesTq, null);
            });
        }
    }

    private void getTreatmentsA() {

        _treatmentsAList = new ArrayList<>();
        _treatmentsACheckBoxes = new ArrayList<>();
        _treatmentsACheckBoxes.add(_checkBoxIntact);
        _treatmentsACheckBoxes.add(_checkBoxNPA);
        _treatmentsACheckBoxes.add(_checkBoxCRIC);
        _treatmentsACheckBoxes.add(_checkBoxEtTube);
        _treatmentsACheckBoxes.add(_checkBoxSGA);

        //getTreatmentPrefs(_treatmentsACheckBoxes, _treatmentsAList);


        for (int i = 0; i < _treatmentsACheckBoxes.size(); i++) {
            CheckBox _checkBoxSelected = _treatmentsACheckBoxes.get(i);
            _checkBoxSelected.setOnCheckedChangeListener((compoundButton, b) -> {
                if (_checkBoxSelected.isChecked()) {
                    _checkBoxSelected.setBackgroundColor(_pluginContext.getColor(R.color.blue));
                    _treatmentsAList.add(String.valueOf(_checkBoxSelected.getText()));

                } else {
                    _checkBoxSelected.setBackground(_pluginContext.getDrawable(R.drawable.table_borders));
                    _treatmentsAList.remove(_checkBoxSelected.getText());
                }
                PulsePrefs.storeTreatmentCheckBoxPrefs(_treatmentsACheckBoxes, null);


            });
        }
    }

    private void getTreatmentsB() {
        _treatmentsBList = new ArrayList<>();
        _treatmentsBCheckBoxes = new ArrayList<>();
        _treatmentsBCheckBoxes.add(_checkBoxO2);
        _treatmentsBCheckBoxes.add(_checkBoxNeedleD);
        _treatmentsBCheckBoxes.add(_checkBoxChestTube);
        _treatmentsBCheckBoxes.add(_checkBoxChestSeal);

        getTreatmentPrefs(_treatmentsBCheckBoxes, _treatmentsBList);

        for (int i = 0; i < _treatmentsBCheckBoxes.size(); i++) {
            CheckBox _checkBoxSelected = _treatmentsBCheckBoxes.get(i);
            _checkBoxSelected.setOnCheckedChangeListener((compoundButton, b) -> {
                if (_checkBoxSelected.isChecked()) {
                    _checkBoxSelected.setBackgroundColor(_pluginContext.getColor(R.color.blue));
                    _treatmentsBList.add(String.valueOf(_checkBoxSelected.getText()));
                    Log.d("TAG", "getTreatmentsB: " + _treatmentsBList);

                } else {
                    _checkBoxSelected.setBackground(_pluginContext.getDrawable(R.drawable.table_borders));
                    _treatmentsBList.remove(_checkBoxSelected.getText());
                    Log.d("TAG2", "getTreatmentsB: " + _treatmentsBList);
                }
                PulsePrefs.storeTreatmentCheckBoxPrefs(_treatmentsBCheckBoxes, null);
            });
        }
    }

    private void getTreatmentsC() {

        _treatmentsCList = new ArrayList<>();
        _treatmentsCCheckBoxes = new ArrayList<>();
        _treatmentsCCheckBoxes.add(_checkBoxExtremity);
        _treatmentsCCheckBoxes.add(_checkBoxJunc);
        _treatmentsCCheckBoxes.add(_checkBoxTrnc);
        _treatmentsCCheckBoxes.add(_checkBoxDressing);
        _treatmentsCCheckBoxes.add(_checkBoxHemo);
        _treatmentsCCheckBoxes.add(_checkBoxDirectPressure);
        _treatmentsCCheckBoxes.add(_checkBoxOther);

        //getTreatmentPrefs(_treatmentsCCheckBoxes, _treatmentsCList);

        for (int i = 0; i < _treatmentsCCheckBoxes.size(); i++) {
            CheckBox _checkBoxSelected = _treatmentsCCheckBoxes.get(i);
            _checkBoxSelected.setOnCheckedChangeListener((compoundButton, b) -> {
                if (_checkBoxSelected.isChecked()) {
                    _checkBoxSelected.setBackgroundColor(_pluginContext.getColor(R.color.blue));
                    _treatmentsCList.add(String.valueOf(_checkBoxSelected.getText()));

                } else {
                    _checkBoxSelected.setBackground(_pluginContext.getDrawable(R.drawable.table_borders));
                    _treatmentsCList.remove(_checkBoxSelected.getText());
                }
                PulsePrefs.storeTreatmentCheckBoxPrefs(_treatmentsCCheckBoxes, null);

            });
        }
    }

    private void getTreatmentsOther() {

        _treatmentsOtherList = new ArrayList<>();
        _treatmentsOtherCheckBoxes = new ArrayList<>();
        _treatmentsOtherCheckBoxes.add(_checkBoxOPillPack);
        _treatmentsOtherCheckBoxes.add(_checkBoxEyeShield);
        _treatmentsOtherCheckBoxes.add(_checkBoxRight);
        _treatmentsOtherCheckBoxes.add(_checkBoxLeft);
        _treatmentsOtherCheckBoxes.add(_checkBoxSplint);
        _treatmentsOtherCheckBoxes.add(_checkBoxHypo);
        getTreatmentPrefs(_treatmentsOtherCheckBoxes, _treatmentsOtherList);

        for (int i = 0; i < _treatmentsOtherCheckBoxes.size(); i++) {
            CheckBox _checkBoxSelected = _treatmentsOtherCheckBoxes.get(i);
            _checkBoxSelected.setOnCheckedChangeListener((compoundButton, b) -> {
                if (_checkBoxSelected.isChecked()) {
                    _checkBoxSelected.setBackgroundColor(_pluginContext.getColor(R.color.blue));
                    _treatmentsOtherList.add(String.valueOf(_checkBoxSelected.getText()));
                    Log.d("TAG", "getTreatmentsB: " + _treatmentsOtherList);

                } else {
                    _checkBoxSelected.setBackground(_pluginContext.getDrawable(R.drawable.table_borders));
                    _treatmentsOtherList.remove(_checkBoxSelected.getText());
                    Log.d("TAG2", "getTreatmentsB: " + _treatmentsOtherList);
                }
                PulsePrefs.storeTreatmentCheckBoxPrefs(_treatmentsOtherCheckBoxes, null);
            });
        }
    }

    private void getInjuryOtherEditText() {
        _editTextOtherInjury.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                //do what you want on the press of 'done'
                Toast.makeText(_mapView.getContext(), _editTextOtherInjury.getText(), Toast.LENGTH_SHORT).show();
                _mechOfInjuryList.add(String.valueOf(_editTextOtherInjury.getText()));
                PulsePrefs.storeTreatmentCheckBoxPrefs(null, _editTextOtherInjury.getText().toString());
            }
            return false;
        });
    }

    private void getTraumaLocation(int position) {
        _injuryLocationList = new ArrayList<>();
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i <= 107; i++) {
            integerList.add(i);
        }

        boolean goodPosition = false;
        for (int i = 0; i < integerList.size(); i++) {
            if (position == i) {
                imageViewConfirmTraumaGreen.setVisibility(View.GONE);
                textViewBodyPart.setText("BAD SELECTION");
            } else {
                goodPosition = true;
            }
        }

        if (goodPosition) {
            imageViewConfirmTraumaGreen.setVisibility(View.VISIBLE);

            switch (position) {

                case 3:
                case 4:
                case 5:
                    textViewBodyPart.setText("FRONT HEAD");
                    break;

                case 12:
                case 13:
                case 14:
                    textViewBodyPart.setText("BACK HEAD");
                    break;

                case 19:
                case 20:
                    textViewBodyPart.setText("RT UPPER ARM");
                    break;

                case 21:
                case 22:
                case 23:
                    textViewBodyPart.setText("FRONT CHEST");
                    break;

                case 24:
                case 25:
                    textViewBodyPart.setText("LEFT UPPER ARM");
                    break;

                case 28:
                case 29:
                    textViewBodyPart.setText("BACK LFT UPPER ARM");
                    break;

                case 33:
                case 34:
                    textViewBodyPart.setText("BACK RT UPPER ARM");
                    break;

                case 37:
                case 38:
                    textViewBodyPart.setText("RT LOWER ARM");
                    break;

                case 40:
                    textViewBodyPart.setText("ABDOMEN");
                    break;

                case 43:
                case 44:
                    textViewBodyPart.setText("LFT LOWER ARM");
                    break;

                case 56:
                case 57:
                    textViewBodyPart.setText("LFT Upper Leg");
                    break;

                case 58:
                    textViewBodyPart.setText("FRONT PELVIS");

                case 59:
                case 60:
                    textViewBodyPart.setText("RT UPPER LEG");
                    break;

                case 51:
                case 52:
                    textViewBodyPart.setText("BACK RT " + "\n UPPER ARM");
                    break;

                case 46:
                case 47:
                case 63:
                case 64:
                    textViewBodyPart.setText("BACK LFT " + "\n LOWER ARM");
                    break;

                case 70:
                case 71:
                    textViewBodyPart.setText("BACK RT " + "\n LOWER ARM");
                    break;

                case 74:
                case 75:
                case 92:
                case 93:
                    textViewBodyPart.setText("RT LOWER LEG");
                    break;

                case 77:
                case 78:
                case 95:
                case 96:
                    textViewBodyPart.setText("LEFT LOWER LEG");
                    break;

                case 30:
                case 31:
                case 32:
                    textViewBodyPart.setText("UPPER BACK");
                    break;


                case 48:
                case 49:
                case 50:
                    textViewBodyPart.setText("BACK PELVIS");
                    break;

                case 65:
                case 66:
                    textViewBodyPart.setText("BACK LFT UPPER LEG");
                    break;

                case 68:
                case 69:
                    textViewBodyPart.setText("BACK RT UPPER LEG");
                    break;

                case 86:
                case 87:
                case 104:
                case 105:
                    textViewBodyPart.setText("BACK RT LOWER LEG");
                    break;

                case 83:
                case 84:
                case 85:
                case 101:
                case 102:
                    textViewBodyPart.setText("BACK LFT LOWER LEG");
                    break;


                default:
                    textViewBodyPart.setVisibility(View.GONE);
                    imageViewConfirmTraumaGreen.setVisibility(View.GONE);
                    _injuryLocationList.add(String.valueOf(textViewBodyPart.getText()));
                    break;
            }
        }

//        ((TextView) gridView.getChildAt(position)).setTextColor(Color.WHITE);
//        ((TextView) gridView.getChildAt(position)).setTextSize(6);
//        gridView.getChildAt(position).setBackgroundColor(Color.RED);
    }

    private void hideTCCCManOnClick() {
        if (frameLayoutTcccMan.getVisibility() == VISIBLE) {
            imageViewFlipTccc.setVisibility(VISIBLE);
            isTcccManVis = true;
            sharedPref.edit().putBoolean(KEY_CASEVAC_TCCC_VIS, isTcccManVis).apply();

        } else {
            imageViewFlipTccc.setVisibility(GONE);
            isTcccManVis = false;
            llTreatments.setVisibility(View.VISIBLE);
            sharedPref.edit().putBoolean(KEY_CASEVAC_TCCC_VIS, isTcccManVis).apply();

        }
        imageViewFlipTccc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                llTreatments.setVisibility(View.VISIBLE);
                frameLayoutTcccMan.setVisibility(View.GONE);
                imageViewFlipTccc.setVisibility(GONE);
                isTcccManVis = false;
                sharedPref.edit().putBoolean(KEY_CASEVAC_TCCC_VIS, isTcccManVis).apply();

            }
        });
    }


    private void flipTcccViewOnClick() {
        ivSmallTcccman.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isTcccManVis = true;
                llTreatments.setVisibility(View.GONE);
                frameLayoutTcccMan.setVisibility(View.VISIBLE);
                imageViewFlipTccc.setVisibility(VISIBLE);
                sharedPref.edit().putBoolean(KEY_CASEVAC_TCCC_VIS, isTcccManVis).apply();
            }
        });
    }

    public void makeCasevacRequestOnClick(TeamMemberInputs teamMemberCasualty) {
        //todo - move to its own intent util class
        String myUID = teamMemberCasualty.getTmCombatID();
        teamMemberCasualty.setTmCasualty(true);
//        get the team adapter and add extras from team member
        String myPoint = String.valueOf(getMapView().getSelfMarker().getGeoPointMetaData());
        String callsign = teamMemberCasualty.getTmCallsign();
        String type = "b-r-f-h-c";
        String assetType = teamMemberCasualty.getTmAssetType();
        String loc = teamMemberCasualty.getTmLocation();
        String bloodType = teamMemberCasualty.getTmBloodType();
        String freq = "425.225";

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getMapView().getContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("com.atakmap.android.medline.pref_medline_freq", freq);
        editor.putString("com.atakmap.android.medline.pref_medline_callsign", callsign);
        editor.apply();

        Intent intent = new Intent("com.atakmap.android.MED_LINE");
        intent.putExtra("point", myPoint);
        intent.putExtra("title", "title test");
        intent.putExtra("how", "m-g");
        intent.putExtra("type", type);
        intent.putExtra("loc", loc);
        intent.putExtra("type", type);

        intent.putExtra("callsign", callsign);
        intent.putExtra("heart_rate", teamMemberCasualty.getTmHeartRate());
        intent.putExtra("injury_location", textViewBodyPart.getText());
        AtakBroadcast.getInstance().sendBroadcast(intent);

        //TODO - BROCK - we need to add the entire zmist report to the PulseCasevacProcessor
        // - we will add the report to the outgoing XML when we "send"
        PulseMistReport mistReport = new PulseMistReport();
        //tell PulseCotEventProcessor we are ready to setup this casevac.
        PulseCasevacProcessor.getInstance().setCurrentCasevacData(teamMemberCasualty, mistReport);

        //set treatments to broadcast
        setTreatmentInputs(_teamMemberInputs);

    }


    /**************************** TREATMENT SPINNER METHODS *****************************/


    public void setupCombatId(TeamMemberInputs inputs) {
        _treatmentInputs.setCombatID(inputs.combatID);
    }

    public void setIVRoute(int id) {
        try {

            Log.d("TAG", "setIVRoute: " + id);
            switch (id) {
                case 2131296751:
                    _treatmentsTableFluidList.clear();
                    if (!fluidSelected.isEmpty()) {
                        _treatmentsTableFluidList.add(fluidSelected);
                    } else {
                        _treatmentsTableFluidList.add(String.valueOf(spinnerFluid.getSelectedItem()));

                    }
                    _treatmentsTableFluidList.add(String.valueOf(buttonFluidVol.getText()));
                    _treatmentsTableFluidList.add(routeSelectedString);
                    _treatmentsTableFluidList.add(String.valueOf(editTextFluidTime.getText()));
//                    Log.d("TAG", "handleTableTreatments: " + _treatmentsTableFluidList.toString());

                    break;

                case 2131296744:
                    _treatmentsTableBloodList.clear();
                    if (!bloodProductSelected.isEmpty()) {
                        _treatmentsTableBloodList.add(bloodProductSelected);
                    } else {
                        _treatmentsTableBloodList.add(String.valueOf(spinnerBlood1.getSelectedItem()));

                    }
                    _treatmentsTableBloodList.add(String.valueOf(buttonBloodProductVol.getText()));
                    _treatmentsTableBloodList.add(routeSelectedString);
                    _treatmentsTableBloodList.add(String.valueOf(editTextBloodProdTime.getText()));
                    break;

                case 2131296740:
                    _treatmentsTableAnalgesicList.clear();
                    if (!analgesicTypeSelected.isEmpty()) {
                        _treatmentsTableAnalgesicList.add(analgesicTypeSelected);
                    } else {
                        _treatmentsTableAnalgesicList.add(String.valueOf(spinnerAnalgesicType1.getSelectedItem()));
                    }
                    _treatmentsTableAnalgesicList.add(String.valueOf(buttonAnalgesicDose.getText()));
                    _treatmentsTableAnalgesicList.add(routeSelectedString);
                    _treatmentsTableAnalgesicList.add(String.valueOf(editTextAnalgesicTime.getText()));
                    break;

                case 2131296741:
                    _treatmentsTableAntibioticList.clear();
                    if (!antibioticsTypeSelected.isEmpty()) {
                        _treatmentsTableAntibioticList.add(antibioticsTypeSelected);
                    } else {
                        _treatmentsTableAntibioticList.add(String.valueOf(spinnerAntibiotics1.getSelectedItem()));
                    }
                    _treatmentsTableAntibioticList.add(String.valueOf(buttonAntibioticDose.getText()));
                    _treatmentsTableAntibioticList.add(routeSelectedString);
                    _treatmentsTableAntibioticList.add(String.valueOf(editTextAntibioticTime.getText()));
                    break;

            }
            PulsePrefs.storeTreatmentTablePreferences(_treatmentsTableFluidList, _treatmentsTableBloodList,
                    _treatmentsTableAnalgesicList, _treatmentsTableAntibioticList, isTcccManVis);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setTimeStamp(Button buttonDoseClicked) {
        String id = String.valueOf(buttonDoseClicked.getId());
        switch (id) {
            case "2131296366":
                editTextFluidTime.setText(getTime());
                if (editTextFluidTime.getText().toString().equals("")) {
                    _treatmentsTableFluidList.add(String.valueOf(editTextFluidTime.getText()));
                }
                break;

            case "2131296361":
                editTextBloodProdTime.setText(getTime());
                if (editTextBloodProdTime.getText().toString().equals("")) {
                    _treatmentsTableBloodList.add(String.valueOf(editTextBloodProdTime.getText()));
                }
                break;

            case "2131296363":
                editTextAnalgesicTime.setText(getTime());
                if (editTextAnalgesicTime.getText().toString().equals("")) {
                    _treatmentsTableAnalgesicList.add(String.valueOf(editTextAnalgesicTime.getText()));
                }
                break;

            case "2131296364":
                editTextAntibioticTime.setText(getTime());
                if (editTextAntibioticTime.getText().toString().equals("")) {
                    _treatmentsTableAntibioticList.add(String.valueOf(editTextAntibioticTime.getText()));
                }
                break;

            default:
                break;
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setPointOfInjury() {

        getTcccManGridAdapter();
        try {
            Log.d("TAG", "setPointOfInjury_shared: " + sharedPref.getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sharedPref != null) {
            try {

                isTcccManVis = sharedPref.getBoolean(KEY_CASEVAC_TCCC_VIS, true);
                if (isTcccManVis) {
                    frameLayoutTcccMan.setVisibility(VISIBLE);
                } else {
                    frameLayoutTcccMan.setVisibility(GONE);

                }
                textViewBodyPart.setText(sharedPref.getString(KEY_CASEVAC_BODY_PART, ""));
                textViewBodyPart.setVisibility(sharedPref.getInt(KEY_CASEVAC_BODY_PART_VIS, View.GONE));
                textViewBodyPart.setX(sharedPref.getFloat(KEY_CASEVAC_BODY_PART_LOC_X, 0));
                textViewBodyPart.setY(sharedPref.getFloat(KEY_CASEVAC_BODY_PART_LOC_Y, 0));

                iv_donut.setVisibility(sharedPref.getInt(KEY_CASEVAC_INJURY_IV_DONUT_POS, View.GONE));
                iv_donut.setX(sharedPref.getFloat(KEY_CASEVAC_INJURY_IV_DONUT_X, 0));
                iv_donut.setY(sharedPref.getFloat(KEY_CASEVAC_INJURY_IV_DONUT_Y, 0));

                imageViewConfirmTraumaGreen.setVisibility(sharedPref.getInt(KEY_CASEVAC_IV_CONFIRM_CHECK_MARK_VIS, View.GONE));
                imageViewConfirmTraumaGreen.setX(sharedPref.getFloat(KEY_CASEVAC_CONFIRMATION_IV_DONUT_X, 0));
                imageViewConfirmTraumaGreen.setY(sharedPref.getFloat(KEY_CASEVAC_CONFIRMATION_IV_DONUT_Y, 0));

                Log.d("TAG", "setPointOfInjuries_list: " +
                        textViewBodyPart.getText().toString()
                        + "\n" + textViewBodyPart.getVisibility()
                        + "\n" + textViewBodyPart.getX()
                        + "\n" + textViewBodyPart.getY()
                        + "\n" + iv_donut.getVisibility()
                        + "\n" + iv_donut.getX()
                        + "\n" + iv_donut.getY()
                        + "\n" + imageViewConfirmTraumaGreen.getVisibility()
                );

                getTablePreferences();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        gridView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int[] viewCoords = new int[2];
                gridView.getLocationOnScreen(viewCoords);
//                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(240, 240, Gravity.CENTER);
//                iv_donut.setLayoutParams(lp);
                float touchX = event.getX();
                float touchY = event.getY();

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        int position = gridView.pointToPosition((int) touchX, (int) touchY);
                        // Access cell position
                        getTraumaLocation(position);
                        iv_donut.setX(touchX - 75);
                        iv_donut.setY(touchY - 75);
                        iv_donut.setVisibility(View.VISIBLE);
                        textViewBodyPart.setX(touchX - 48);
                        textViewBodyPart.setY(touchY + 42);
                        textViewBodyPart.setVisibility(View.VISIBLE);

                    case MotionEvent.ACTION_UP:
                        imageViewConfirmTraumaGreen.setX(touchX + 16);
                        imageViewConfirmTraumaGreen.setY(touchY - 75);

                        PulsePrefs.storeInjuryLocationPrefs(
                                textViewBodyPart.getText().toString(),
                                textViewBodyPart.getVisibility(),
                                textViewBodyPart.getX(),
                                textViewBodyPart.getY(),

                                iv_donut.getVisibility(),
                                iv_donut.getX(),
                                iv_donut.getY(),

                                imageViewConfirmTraumaGreen.getVisibility(),
                                imageViewConfirmTraumaGreen.getX(),
                                imageViewConfirmTraumaGreen.getY());

                        imageViewConfirmTraumaGreen.setOnClickListener(new OnClickListener() {
                            @SuppressLint("WrongConstant")
                            @Override
                            public void onClick(View view) {
                                imageViewFlipTccc.setVisibility(GONE);
                                isTcccManVis = false;
                                ivSmallTcccman.requestFocus();
                                ivSmallTcccman.setVisibility(VISIBLE);
                                _injuryLocationList.add(String.valueOf(textViewBodyPart.getText()));
                                frameLayoutTcccMan.setVisibility(View.GONE);
                                llTreatments.setVisibility(View.VISIBLE);
                                flipTcccViewOnClick();

                                getFluidName();
                                getBloodProductSelector();
                                getAnalgesicChangeListener();
                                getAntibioticsSelector();
                                getVolumeAndDose(buttonsDoseList);
                                getRouteSpinner(spinnerRouteList);
                                _treatmentInputs.setInjuryLocation(_injuryLocationList);

                            }
                        });
                        break;
                }


                return true;
            }
        });
    }

    public void getAnalgesicChangeListener() {

//        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> analgesicAdapterOne = ArrayAdapter.createFromResource(_view.getContext(),
                R.array.analgesic_array, R.layout.spinner_item_light);
        // Apply the adapter to the spinner
        spinnerAnalgesicType1.setAdapter(analgesicAdapterOne);
        spinnerAnalgesicType1.getSelectedItem();

        spinnerAnalgesicType1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                analgesicTypeSelected = (String) parent.getItemAtPosition(position);
                if (!_treatmentsTableAnalgesicList.isEmpty()) {
                    _treatmentsTableAnalgesicList.remove(0);
                }
                _treatmentsTableAnalgesicList.add(0, analgesicTypeSelected);
                PulsePrefs.storeTreatmentTablePreferences(_treatmentsTableFluidList, _treatmentsTableBloodList,
                        _treatmentsTableAnalgesicList, _treatmentsTableAntibioticList, isTcccManVis);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getAntibioticsSelector() {
        ArrayAdapter<CharSequence> antibioticsAdapterOne = ArrayAdapter.createFromResource(_pluginContext,
                R.array.antibiotics_array, R.layout.spinner_item);
        spinnerAntibiotics1.setAdapter(antibioticsAdapterOne);
        spinnerAntibiotics1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                antibioticsTypeSelected = (String) parent.getItemAtPosition(position);
                if (!_treatmentsTableAntibioticList.isEmpty()) {
                    _treatmentsTableAntibioticList.remove(0);
                }
                _treatmentsTableAntibioticList.add(0, antibioticsTypeSelected);
                PulsePrefs.storeTreatmentTablePreferences(_treatmentsTableFluidList, _treatmentsTableBloodList,
                        _treatmentsTableAnalgesicList, _treatmentsTableAntibioticList, isTcccManVis);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getBloodProductSelector() {

        ArrayAdapter<CharSequence> bloodAdapterOne = ArrayAdapter.createFromResource(_pluginContext,
                R.array.blood_product_array, R.layout.spinner_item);
        spinnerBlood1.setAdapter(bloodAdapterOne);
        spinnerBlood1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodProductSelected = (String) parent.getItemAtPosition(position);
//                Log.i(TAG, "onAssetSelected: " + _paxTypeSelection);
                if (!_treatmentsTableBloodList.isEmpty()) {
                    _treatmentsTableBloodList.remove(0);
                }
                _treatmentsTableBloodList.add(0, bloodProductSelected);
                PulsePrefs.storeTreatmentTablePreferences(_treatmentsTableFluidList, _treatmentsTableBloodList,
                        _treatmentsTableAnalgesicList, _treatmentsTableAntibioticList, isTcccManVis);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerBlood1.setSelection(0);

    }

    public void getFluidName() {

        ArrayAdapter<CharSequence> bloodAdapterOne = ArrayAdapter.createFromResource(_pluginContext,
                R.array.fluid_array, R.layout.spinner_item_light);
        spinnerFluid.setAdapter(bloodAdapterOne);
        spinnerFluid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fluidSelected = (String) parent.getItemAtPosition(position);
                if (!_treatmentsTableFluidList.isEmpty()) {
                    _treatmentsTableFluidList.remove(0);
                }
                _treatmentsTableFluidList.add(0, fluidSelected);
                PulsePrefs.storeTreatmentTablePreferences(_treatmentsTableFluidList, _treatmentsTableBloodList,
                        _treatmentsTableAnalgesicList, _treatmentsTableAntibioticList, isTcccManVis);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerFluid.setSelection(0);
    }

    public void getDoseUnitSpinner() {

        ArrayAdapter<CharSequence> doseAdapter = ArrayAdapter.createFromResource(_pluginContext,
                R.array.dose_unit_array, R.layout.spinner_dose_measurement);
        spinnerDoseUnit.setAdapter(doseAdapter);
        spinnerDoseUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doseUnitSelected = (String) parent.getItemAtPosition(position);
                textViewDoseMeasurement.setText(doseUnitSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerDoseUnit.setSelection(0);
    }

    public void getRouteSpinner(List<PluginSpinner> routeSelected) {

        for (int i = 0; i < routeSelected.size(); i++) {
            ArrayAdapter<CharSequence> doseAdapter;
            if (i == 0 || i == 2) {
                doseAdapter = ArrayAdapter.createFromResource(_pluginContext,
                        R.array.route_array, R.layout.spinner_item_light);
            } else {
                doseAdapter = ArrayAdapter.createFromResource(_pluginContext,
                        R.array.route_array, R.layout.spinner_item);
            }
            routeSelected.get(i).setAdapter(doseAdapter);
            int finalI = i;
            routeSelected.get(i).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    routeSelectedString = (String) parent.getItemAtPosition(position);
                    setIVRoute(routeSelected.get(finalI).getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            routeSelected.get(i).setSelection(0);
        }
    }

    private void getTQPrefs(List<CheckBox> checkBoxes, List<String> treatmentsStringList, List<String> tqTimeStampList) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(
                MapView.getMapView().getContext());

        for (int i = 0; i < checkBoxes.size(); i++) {
            boolean checkBoxChecked = preferences.getBoolean(KEY_CASEVAC_TQ_CHECKED + checkBoxes.get(i).getId(), false);
            String timeStamp = preferences.getString(KEY_CASEVAC_TQ_TIME + checkBoxes.get(i).getId(), "");
            checkBoxes.get(i).setChecked(checkBoxChecked);
            if (checkBoxChecked) {
                checkBoxes.get(i).setBackgroundColor(_pluginContext.getColor(R.color.blue));
                checkBoxes.get(i).setText(timeStamp);
                treatmentsStringList.add(String.valueOf(checkBoxes.get(i).getText()));

            } else {
                checkBoxes.get(i).setBackground(_pluginContext.getDrawable(R.drawable.table_borders));
                treatmentsStringList.remove(checkBoxes.get(i).getText());
            }
        }
    }

    public void getVolumeAndDose(List<Button> buttonList) {
        for (int i = 0; i < buttonList.size(); i++) {
            Button buttonClicked = buttonList.get(i);
            buttonClicked.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDoseKeyPad(buttonClicked);
                    _linearLayoutTreatmentTable.setVisibility(GONE);

                }
            });
        }
    }

    private void getTreatmentPrefs(List<CheckBox> checkBoxes, List<String> treatmentsStringList) {

        for (int i = 0; i < checkBoxes.size(); i++) {
            boolean checkBoxChecked = sharedPref.getBoolean(PulsePrefs.KEY_CASEVAC_TREATMENTS_CHECKBOXES + checkBoxes.get(i).getText(), false);
            checkBoxes.get(i).setChecked(checkBoxChecked);
            if (checkBoxChecked) {
                checkBoxes.get(i).setBackgroundColor(_pluginContext.getColor(R.color.blue));
                treatmentsStringList.add(String.valueOf(checkBoxes.get(i).getText()));

            } else {
                checkBoxes.get(i).setBackground(_pluginContext.getDrawable(R.drawable.table_borders));
                treatmentsStringList.remove(checkBoxes.get(i).getText());
            }
        }
    }

    public void getTablePreferences() {
        try {

            isTcccManVis = sharedPref.getBoolean(KEY_CASEVAC_TCCC_VIS, true);
            Log.d("TAG", "getTablePreferences: " + isTcccManVis);

            if (isTcccManVis) {
                frameLayoutTcccMan.setVisibility(VISIBLE);
                ivSmallTcccman.setVisibility(GONE);
                llTreatments.setVisibility(View.GONE);
            } else {
                frameLayoutTcccMan.setVisibility(GONE);
                llTreatments.setVisibility(View.VISIBLE);
                ivSmallTcccman.setVisibility(VISIBLE);
                flipTcccViewOnClick();
                iv_donut.setVisibility(GONE);
                imageViewConfirmTraumaGreen.setVisibility(GONE);
            }
            getFluidName();
            getBloodProductSelector();
            getAnalgesicChangeListener();
            getAntibioticsSelector();
            getVolumeAndDose(buttonsDoseList);
            getRouteSpinner(spinnerRouteList);
            //Gson gson = new Gson();

            String fluidList = sharedPref.getString(KEY_CASEVAC_FLUID_LIST, "");
            String bloodProductList = sharedPref.getString(KEY_CASEVAC_BLOOD_PROD_LIST, "");
            String analgesicList = sharedPref.getString(KEY_CASEVAC_ANALGESIC_LIST, "");
            String antibioticList = sharedPref.getString(KEY_CASEVAC_ANTIBIOTIC_LIST, "");

            //Type type = new TypeToken<ArrayList<String>>() {}.getType();
            //BROCK- Why are we doing GSON inside the linear layout?
            //_treatmentsTableFluidList.addAll(Objects.requireNonNull(gson.fromJson(fluidList, type)));
           //_treatmentsTableBloodList.addAll(Objects.requireNonNull(gson.fromJson(bloodProductList, type)));
            //_treatmentsTableAnalgesicList.addAll(Objects.requireNonNull(gson.fromJson(analgesicList, type)));
            //_treatmentsTableAntibioticList.addAll(Objects.requireNonNull(gson.fromJson(antibioticList, type)));

            if (_treatmentsTableFluidList.size() > 0) {
                ArrayAdapter<String> fluidAdapter = (ArrayAdapter<String>) spinnerFluid.getAdapter();
                int fluidPosition = fluidAdapter.getPosition(_treatmentsTableFluidList.get(0));
                spinnerFluid.setSelection(fluidPosition);

                buttonFluidVol.setText(_treatmentsTableFluidList.get(1));

                ArrayAdapter<String> fluidRouteAdapter = (ArrayAdapter<String>) spinnerFluidRoute.getAdapter();
                int fluidRoutePosition = fluidRouteAdapter.getPosition(_treatmentsTableFluidList.get(2));
                spinnerFluidRoute.setSelection(fluidRoutePosition);

                editTextFluidTime.setText(_treatmentsTableFluidList.get(3));
            }
            if (_treatmentsTableBloodList.size() > 0) {
                ArrayAdapter<String> fluidAdapter = (ArrayAdapter<String>) spinnerBlood1.getAdapter();
                int fluidPosition = fluidAdapter.getPosition(_treatmentsTableBloodList.get(0));
                spinnerBlood1.setSelection(fluidPosition);

                buttonBloodProductVol.setText(_treatmentsTableBloodList.get(1));

                ArrayAdapter<String> bloodRouteAdapter = (ArrayAdapter<String>) spinnerBloodRoute.getAdapter();
                int bloodRoutePosition = bloodRouteAdapter.getPosition(_treatmentsTableBloodList.get(2));
                spinnerBloodRoute.setSelection(bloodRoutePosition);

                editTextBloodProdTime.setText(_treatmentsTableBloodList.get(3));
            }
            if (_treatmentsTableAnalgesicList.size() > 0) {
                ArrayAdapter<String> fluidAdapter = (ArrayAdapter<String>) spinnerAnalgesicType1.getAdapter();
                int fluidPosition = fluidAdapter.getPosition(_treatmentsTableAnalgesicList.get(0));
                spinnerAnalgesicType1.setSelection(fluidPosition);

                buttonAnalgesicDose.setText(_treatmentsTableAnalgesicList.get(1));

                ArrayAdapter<String> analgesicRouteAdapter = (ArrayAdapter<String>) spinnerAnalgesicRoute.getAdapter();
                int analgesicRoutePosition = analgesicRouteAdapter.getPosition(_treatmentsTableAnalgesicList.get(2));
                spinnerAnalgesicRoute.setSelection(analgesicRoutePosition);

                editTextAnalgesicTime.setText(_treatmentsTableAnalgesicList.get(3));
            }

            if (_treatmentsTableAntibioticList.size() > 0) {
                ArrayAdapter<String> antibiotics1Adapter = (ArrayAdapter<String>) spinnerAntibiotics1.getAdapter();
                int fluidPosition = antibiotics1Adapter.getPosition(_treatmentsTableAntibioticList.get(0));
                spinnerAntibiotics1.setSelection(fluidPosition);

                buttonAntibioticDose.setText(_treatmentsTableAntibioticList.get(1));

                ArrayAdapter<String> antibioticRouteAdapter = (ArrayAdapter<String>) spinnerAntibioticRoute.getAdapter();
                int antibioticRouteAdapterPosition = antibioticRouteAdapter.getPosition(_treatmentsTableAntibioticList.get(2));
                spinnerAntibioticRoute.setSelection(antibioticRouteAdapterPosition);

                editTextAntibioticTime.setText(_treatmentsTableAntibioticList.get(3));
            }
            Log.d("TAG", "getTablePreferences_Buttons: " + "\n" + buttonFluidVol.getText() + "\n " + buttonBloodProductVol.getText() + " \n" + buttonAnalgesicDose.getText() + "\n" + buttonAntibioticDose.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetCheckBoxes() {

        for (int i = 0; i < _mechOfInjuryCheckBoxes.size(); i++) {
            _mechOfInjuryCheckBoxes.get(i).setChecked(false);
        }
        for (int i = 0; i < _treatmentsACheckBoxes.size(); i++) {
            _treatmentsACheckBoxes.get(i).setChecked(false);
        }
        for (int i = 0; i < _treatmentsBCheckBoxes.size(); i++) {
            _treatmentsBCheckBoxes.get(i).setChecked(false);
        }
        for (int i = 0; i < _treatmentsCCheckBoxes.size(); i++) {
            _treatmentsCCheckBoxes.get(i).setChecked(false);
        }
        for (int i = 0; i < _treatmentsOtherCheckBoxes.size(); i++) {
            _treatmentsOtherCheckBoxes.get(i).setChecked(false);
        }
        for (int i = 0; i < checkBoxesTq.size(); i++) {
            checkBoxesTq.get(i).setChecked(false);
        }

    }

    public void clearTableData() {

        spinnerFluid.setSelection(0);
        spinnerBlood1.setSelection(0);
        spinnerAnalgesicType1.setSelection(0);
        spinnerAntibiotics1.setSelection(0);

        buttonFluidVol.setText("");
        buttonBloodProductVol.setText("");
        buttonAnalgesicDose.setText("");
        buttonAntibioticDose.setText("");

        spinnerFluidRoute.setSelection(0);
        spinnerBloodRoute.setSelection(0);
        spinnerAnalgesicRoute.setSelection(0);
        spinnerAntibioticRoute.setSelection(0);

        editTextFluidTime.setText("");
        editTextBloodProdTime.setText("");
        editTextAnalgesicTime.setText("");
        editTextAntibioticTime.setText("");
    }

    public void clearTreatmentLists() {

        iv_donut.setVisibility(GONE);
        imageViewConfirmTraumaGreen.setVisibility(GONE);
        textViewBodyPart.setText("");
        textViewBodyPart.setVisibility(GONE);

        _mechOfInjuryList.clear();
        _tqTreatmentsList.clear();
        _treatmentsTableFluidList.clear();
        _treatmentsTableBloodList.clear();
        _treatmentsTableAnalgesicList.clear();
        _treatmentsTableAntibioticList.clear();

        _treatmentsAList.clear();
        _treatmentsBList.clear();
        _treatmentsCList.clear();
        _treatmentsOtherList.clear();


    }

    public void restoreDefaultSelectionsOnClick() {
        buttonResetPrefs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    isTcccManVis = true;

                    clearTreatmentLists();
                    resetCheckBoxes();
                    clearTableData();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                _view.refreshDrawableState();
                sharedPref.edit()
                        .putBoolean(KEY_CASEVAC_TREATMENTS_CHECKBOXES, false)
                        .putBoolean(KEY_CASEVAC_TQ_CHECKED, false)
                        .remove(KEY_CASEVAC_TQ_TIME)

                        .remove(KEY_CASEVAC_BODY_PART)
                        .remove(KEY_CASEVAC_BODY_PART_LOC_X)
                        .remove(KEY_CASEVAC_BODY_PART_LOC_Y)
                        .putBoolean(KEY_CASEVAC_BODY_PART_VIS, false)

                        .remove(KEY_CASEVAC_INJURY_IV_DONUT_POS)
                        .remove(KEY_CASEVAC_INJURY_IV_DONUT_X)
                        .remove(KEY_CASEVAC_INJURY_IV_DONUT_Y)
                        .putBoolean(KEY_CASEVAC_IV_CONFIRM_CHECK_MARK_VIS, false)

                        .remove(KEY_CASEVAC_CONFIRMATION_IV_DONUT_X)
                        .remove(KEY_CASEVAC_CONFIRMATION_IV_DONUT_Y)
                        .putBoolean(KEY_CASEVAC_IV_CONFIRM_CHECK_MARK_VIS, false)

                        .remove(KEY_CASEVAC_FLUID_LIST)
                        .remove(KEY_CASEVAC_BLOOD_PROD_LIST)
                        .remove(KEY_CASEVAC_ANALGESIC_LIST)
                        .remove(KEY_CASEVAC_ANTIBIOTIC_LIST)
                        .putBoolean(KEY_CASEVAC_TCCC_VIS, isTcccManVis)
                        .apply();
                frameLayoutTcccMan.setVisibility(VISIBLE);
                ivSmallTcccman.setVisibility(GONE);
                imageViewFlipTccc.setVisibility(VISIBLE);
                Toast.makeText(_pluginContext, "Clicked Reset", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void setTreatmentInputs(TeamMemberInputs inputs) {
        _treatmentInputs.setCombatID(Integer.parseInt(inputs.getTmCombatID()));
        _treatmentInputs.setMechOfInjury(_mechOfInjuryList);
        _treatmentInputs.setTqTreatmentsList(_tqTreatmentsList);
        _treatmentInputs.setTreatmentsA(_treatmentsAList);
        _treatmentInputs.setTreatmentsB(_treatmentsBList);
        _treatmentInputs.setTreatmentsC(_treatmentsCList);
        _treatmentInputs.setTreatmentsOther(_treatmentsOtherList);

        _treatmentInputs.setInjuryLocation(_injuryLocationList);

        _treatmentInputs.setFluidTreatment(_treatmentsTableFluidList);
        _treatmentInputs.setBloodTreatment(_treatmentsTableBloodList);
        _treatmentInputs.setAnalgesicTreatment(_treatmentsTableAnalgesicList);
        _treatmentInputs.setAntibioticTreatment(_treatmentsTableAntibioticList);

        Log.d("TAG", "setTreatmentInputs: "
                + _treatmentInputs.getCombatID()
                + "\n" + _treatmentInputs.getMechOfInjury()
                + "\n" + _treatmentInputs.getTqTreatmentsList()
                + "\n" + _treatmentInputs.getTreatmentsA()
                + "\n" + _treatmentInputs.getTreatmentsB()
                + "\n" + _treatmentInputs.getTreatmentsC()
                + "\n" + _treatmentInputs.getTreatmentsOther()
                + "\n" + _treatmentInputs.getInjuryLocation()
                + "\n" + _treatmentInputs.getFluidTreatment()
                + "\n" + _treatmentInputs.getBloodTreatment()
                + "\n" + _treatmentInputs.getAnalgesicTreatment()
                + "\n" + _treatmentInputs.getAntibioticTreatment()
        );

        //TODO - GSON should NEVER NEVER NEVER be found in view code.
        //Set the values
        /*Gson gson = new Gson();
        //TODO -  we can serialize this entire struct and put it into the DB as a single string.
        String jsonCombatId = gson.toJson(_treatmentInputs.getCombatID());
        String jsonMechInjury = gson.toJson(_treatmentInputs.getMechOfInjury());
        String jsonTqList = gson.toJson(_treatmentInputs.getTqTreatmentsList());
        String jsonTreatmentsA = gson.toJson(_treatmentInputs.getTreatmentsA());
        String jsonTreatmentsB = gson.toJson(_treatmentInputs.getTreatmentsB());
        String jsonTreatmentsC = gson.toJson(_treatmentInputs.getTreatmentsC());
        String jsonTreatmentsOther = gson.toJson(_treatmentInputs.getTreatmentsOther());
        String jsonInjuryLocation = gson.toJson(_treatmentInputs.getInjuryLocation());
        String jsonFluidTreatmentList = gson.toJson(_treatmentInputs.getFluidTreatment());
        String jsonBloodTreatmentList = gson.toJson(_treatmentInputs.getBloodTreatment());
        String jsonAnalgesicTreatmentList = gson.toJson(_treatmentInputs.getAnalgesicTreatment());
        String jsonAntibioticTreatmentList = gson.toJson(_treatmentInputs.getAntibioticTreatment());

        DBManager dbManager = new DBManager(_pluginContext);
        dbManager.open();

        dbManager.insertPatientTreatment(jsonCombatId,
                jsonMechInjury,
                jsonTqList,
                jsonTreatmentsA,
                jsonTreatmentsB,
                jsonTreatmentsC,
                jsonTreatmentsOther,
                jsonInjuryLocation,
                jsonFluidTreatmentList,
                jsonBloodTreatmentList,
                jsonAnalgesicTreatmentList,
                jsonAntibioticTreatmentList
        );
        Log.d("TAG", "setTreatmentInputs: " +

                jsonMechInjury +
                jsonTqList +
                jsonTreatmentsA +
                jsonTreatmentsB +
                jsonTreatmentsC +
                jsonTreatmentsOther +
                jsonInjuryLocation +
                jsonFluidTreatmentList +
                jsonBloodTreatmentList +
                jsonAnalgesicTreatmentList +
                jsonAntibioticTreatmentList
        );*/

    }

}


