package com.example.ninokhodabandeh.ui.Interfaces;

import com.example.ninokhodabandeh.ui.Models.UserParameters;
import com.example.ninokhodabandeh.ui.Services.ApiServices;

import java.util.ArrayList;

/**
 * Created by nino.khodabandeh on 8/29/2014.
 */
public interface IApiService {
    public ArrayList<ApiServices.ApiResultModel> getResultFromApi(UserParameters parameters);
}
