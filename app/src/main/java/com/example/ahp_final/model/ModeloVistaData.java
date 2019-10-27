package com.example.ahp_final.model;

import java.io.Serializable;
import java.util.Map;

public class ModeloVistaData implements Serializable {

    public static final String DATA_KEY = "data_key";

    public Map<String, Float> CriterioValorMap;
    public Map<String, Float> AlternativaValorMap;

}
