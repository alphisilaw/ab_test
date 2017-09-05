package com.web.abt.m.service.impl;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.web.abt.m.dao.KPIDataDao;
import com.web.abt.m.model.KPIData;
import com.web.abt.m.model.PvUvData;
import com.web.abt.m.service.DataViewService;
import com.web.abt.m.util.CommonUtil;

public class DataViewServiceImpl implements DataViewService {
	
	private DecimalFormat df = new DecimalFormat("######0.00");   

	
	@Override
	public Map<Integer, Map<String, Object>> getKPIData(Integer caseId, Date begin, Date end) {
		
		List<KPIData> kpiData = KPIDataDao.getInstance().getKpiAllByTestId(caseId, begin, end);
		List<PvUvData> pvuvData = KPIDataDao.getInstance().getPvUvByTestId(caseId, begin, end);
		
		Set<Integer> versionIds = new HashSet<Integer>();
		Map<Integer, Double> regitCountMap = new HashMap<Integer, Double>();//注册数
		Map<Integer, Integer> pvMap = new HashMap<Integer, Integer>();//pv
		Map<Integer, Integer> uvMap = new HashMap<Integer, Integer>();//uv
		Map<Integer, Integer> clickMap = new HashMap<Integer, Integer>();//click
		Map<Integer, Double> verpassCountMap = new HashMap<Integer, Double>();//验证通过数
		Map<Integer, Double> selfSevcIncomeMap = new HashMap<Integer, Double>();//自助收入
		Map<Integer, Double> offlineIncomeMap = new HashMap<Integer, Double>();//线下收入
		Map<Integer, Double> telsellIncomeMap = new HashMap<Integer, Double>();//电销收入
		Map<Integer, Integer> selfSevcUvMap = new HashMap<Integer, Integer>();//自助收入人数
		Map<Integer, Integer> offlineUvMap = new HashMap<Integer, Integer>();//线下收入人数
		Map<Integer, Integer> telsellUvMap = new HashMap<Integer, Integer>();//电销收入人数
		for (KPIData data : kpiData) {
			
			Integer testversionid = data.getTestversionid();
			if (!versionIds.contains(testversionid)) {
				versionIds.add(testversionid);
			}

			String kpicode = data.getKpicode();
			String kpin = data.getKpin();
			int uv = CommonUtil.evalInt(data.getUv(), 0);
			if (StringUtils.equalsIgnoreCase("regitCount", kpicode)) {
				double regitCount = CommonUtil.evalDouble(regitCountMap.get(testversionid), 0);
				regitCountMap.put(testversionid, regitCount+CommonUtil.evalDouble(kpin, 0));
			} else if (StringUtils.equalsIgnoreCase("verpassCount", kpicode)) {
				double verpassCount = CommonUtil.evalDouble(verpassCountMap.get(testversionid), 0);
				verpassCountMap.put(testversionid, verpassCount+CommonUtil.evalDouble(kpin, 0));
			} else if (StringUtils.equalsIgnoreCase("selfSevcIncome", kpicode)) {
				double selfSevcIncome = CommonUtil.evalDouble(selfSevcIncomeMap.get(testversionid), 0d);
				selfSevcIncomeMap.put(testversionid, selfSevcIncome+CommonUtil.evalDouble(kpin, 0d));
				
				int selfSevcUv = CommonUtil.evalInt(selfSevcUvMap.get(testversionid), 0);
				selfSevcUvMap.put(testversionid, selfSevcUv+uv);
			} else if (StringUtils.equalsIgnoreCase("offlineIncome", kpicode)) {
				double offlineIncome = CommonUtil.evalDouble(offlineIncomeMap.get(testversionid), 0d);
				offlineIncomeMap.put(testversionid, offlineIncome+CommonUtil.evalDouble(kpin, 0d));
				
				int offlineUv = CommonUtil.evalInt(offlineUvMap.get(testversionid), 0);
				offlineUvMap.put(testversionid, offlineUv+uv);
			} else if (StringUtils.equalsIgnoreCase("telsellIncome", kpicode)) {
				double telsellIncome = CommonUtil.evalDouble(telsellIncomeMap.get(testversionid), 0d);
				telsellIncomeMap.put(testversionid, telsellIncome+CommonUtil.evalDouble(kpin, 0d));

				int telsellUv = CommonUtil.evalInt(telsellUvMap.get(testversionid), 0);
				telsellUvMap.put(testversionid, telsellUv+uv);
			}
		}
		for (PvUvData data : pvuvData) {
			
			Integer testversionid = data.getTestversionid();
			if (!versionIds.contains(testversionid)) {
				versionIds.add(testversionid);
			}
			if ("0".equals(data.getClickid())) {
				Integer pv = data.getPv();
				Integer uv = data.getUv();
				pvMap.put(testversionid, CommonUtil.evalInt(pvMap.get(testversionid), 0) + CommonUtil.evalInt(pv, 0));
				uvMap.put(testversionid, CommonUtil.evalInt(uvMap.get(testversionid), 0) + CommonUtil.evalInt(uv, 0));
			} else {
				Integer uv = data.getUv();
				clickMap.put(testversionid, CommonUtil.evalInt(clickMap.get(testversionid), 0) + CommonUtil.evalInt(uv, 0));
			}
		}

		Map<Integer, Map<String, Object>> resultMap = new HashMap<Integer, Map<String, Object>>();
		for (Integer testversionid : versionIds) {
			
			Map<String, Object> rows = new HashMap<String, Object>();
			int pv = CommonUtil.evalInt(pvMap.get(testversionid), 0);
			int uv = CommonUtil.evalInt(uvMap.get(testversionid), 0);
			int click = CommonUtil.evalInt(clickMap.get(testversionid), 0);
			double regitCount = CommonUtil.evalDouble(regitCountMap.get(testversionid), 0d);
			double verpassCount = CommonUtil.evalDouble(verpassCountMap.get(testversionid), 0d);
			double selfSevcIncome = CommonUtil.evalDouble(selfSevcIncomeMap.get(testversionid), 0d);
			double offlineIncome = CommonUtil.evalDouble(offlineIncomeMap.get(testversionid), 0d);
			double telsellIncome = CommonUtil.evalDouble(telsellIncomeMap.get(testversionid), 0d);
			int selfSevcUv = CommonUtil.evalInt(selfSevcUvMap.get(testversionid), 0);
			int offlineUv = CommonUtil.evalInt(offlineUvMap.get(testversionid), 0);
			int telsellUv = CommonUtil.evalInt(telsellUvMap.get(testversionid), 0);
			
			rows.put("pv", String.valueOf(pv));//0访问量

			rows.put("uv", String.valueOf(uv));//1访客数

			rows.put("click", String.valueOf(click));//1点击数

			rows.put("regitCount", String.valueOf((int) regitCount));//2注册数
			
			String regitPercent = df.format(uv==0? 0d : regitCount/uv*100) + "%";
			rows.put("regitPercent", regitPercent);//3注册率
			
			rows.put("verpassCount",String.valueOf((int) verpassCount));//号码验证数
			
			String verpassPercent = df.format(regitCount==0? 0d : verpassCount/regitCount*100) + "%";
			rows.put("verpassPercent", verpassPercent);//4号码验证率
			
			rows.put("selfSevcIncome", df.format(selfSevcIncome));//自助收入
			
			rows.put("offlineIncome", df.format(offlineIncome));//线下收入
			
			rows.put("telsellIncome", df.format(telsellIncome));//电销收入

			rows.put("selfSevcUv", String.valueOf(selfSevcUv));//自助付款人数

			rows.put("offlineUv", String.valueOf(offlineUv));//线下付款人数

			rows.put("telsellUv", String.valueOf(telsellUv));//电销付款人数
			
			String selfPercent = selfSevcUv==0 ? "/" : df.format(selfSevcIncome/selfSevcUv);
			rows.put("selfPercent", selfPercent);//自助人均收入
			
			String offlinePercent = offlineUv==0 ? "/" : df.format(offlineIncome/offlineUv);
			rows.put("offlinePercent", offlinePercent);//线下人均收入
			
			String telsellPercent = telsellUv==0 ? "/" : df.format(telsellIncome/telsellUv);
			rows.put("telsellPercent", telsellPercent);//电销人均收入
			
			double totalIncome = selfSevcIncome + offlineIncome + telsellIncome;
			rows.put("totalIncome", df.format(totalIncome));//总收入
			
			resultMap.put(testversionid, rows);
		}
		return resultMap;
	}

}
