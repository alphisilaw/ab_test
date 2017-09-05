package com.zhenai.channel_manager.service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zhenai.channel_manager.dao.KPIDataDao;
import com.zhenai.channel_manager.moudel.KPIData;
import com.zhenai.channel_manager.moudel.PvUvData;
import com.zhenai.channel_manager.util.CommonUtil;

@Service("dataViewService")
public class DataViewServiceImpl implements DataViewService {
	
	private DecimalFormat df = new DecimalFormat("######0.00");   

	@Resource
	private KPIDataDao kpiDataDao;
	
	@Override
	public Map<Integer, Map<String, Object>> getKPIData(Integer caseId, Date begin, Date end) {
		
		List<KPIData> kpiData = kpiDataDao.getKpiAllByTestId(caseId, begin, end);
		List<PvUvData> pvuvData = kpiDataDao.getPvUvByTestId(caseId, begin, end);
		
		Set<Integer> versionIds = new HashSet<Integer>();
		Map<Integer, Double> regitCountMap = new HashMap<Integer, Double>();//注册数
		Map<Integer, Integer> pvMap = new HashMap<Integer, Integer>();//pv
		Map<Integer, Integer> uvMap = new HashMap<Integer, Integer>();//uv
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
			
			Integer pv = data.getPv();
			Integer uv = data.getUv();
			pvMap.put(testversionid, CommonUtil.evalInt(pvMap.get(testversionid), 0) + CommonUtil.evalInt(pv, 0));
			uvMap.put(testversionid, CommonUtil.evalInt(uvMap.get(testversionid), 0) + CommonUtil.evalInt(uv, 0));
		}

		Map<Integer, Map<String, Object>> resultMap = new HashMap<Integer, Map<String, Object>>();
		for (Integer testversionid : versionIds) {
			
			Map<String, Object> rows = new HashMap<String, Object>();
			int pv = CommonUtil.evalInt(pvMap.get(testversionid), 0);
			int uv = CommonUtil.evalInt(uvMap.get(testversionid), 0);
			double regitCount = CommonUtil.evalDouble(regitCountMap.get(testversionid), 0);
			double verpassCount = CommonUtil.evalDouble(verpassCountMap.get(testversionid), 0);
			double selfSevcIncome = CommonUtil.evalDouble(selfSevcIncomeMap.get(testversionid), 0d);
			double offlineIncome = CommonUtil.evalDouble(offlineIncomeMap.get(testversionid), 0d);
			double telsellIncome = CommonUtil.evalDouble(telsellIncomeMap.get(testversionid), 0d);
			int selfSevcUv = CommonUtil.evalInt(selfSevcUvMap.get(testversionid), 0);
			int offlineUv = CommonUtil.evalInt(offlineUvMap.get(testversionid), 0);
			int telsellUv = CommonUtil.evalInt(telsellUvMap.get(testversionid), 0);
			
			rows.put("pv", String.valueOf(pv));//0访问量

			rows.put("uv", String.valueOf(uv));//1访客数

			rows.put("regitCount", String.valueOf((int) regitCount));//2注册数
			
			String regitPercent = df.format(uv==0? 0d : regitCount/uv*100) + "%";
			rows.put("regitPercent", regitPercent);//3注册率
			
			rows.put("verpassCount", String.valueOf((int)verpassCount));//验证数
			
			String verpassPercent = df.format(regitCount==0? 0d : verpassCount/regitCount*100) + "%";
			rows.put("verpassPercent", verpassPercent);//4号码验证率
			
			rows.put("selfSevcIncome", df.format(selfSevcIncome));//7自助收入
			
			rows.put("offlineIncome", df.format(offlineIncome));//8线下收入
			
			rows.put("telsellIncome", df.format(telsellIncome));//9电销收入
			
			double totalIncome = selfSevcIncome + offlineIncome + telsellIncome;
			rows.put("totalIncome", df.format(totalIncome));//5总收入
			
			int totalUv = selfSevcUv + offlineUv + telsellUv;
			String totalPercent = totalUv==0 ? "/" : df.format(totalIncome/totalUv);
			rows.put("totalPercent", totalPercent);//6人均收入
			
			resultMap.put(testversionid, rows);
		}
		return resultMap;

	}
	
	@Override
	public Integer getUvByVersionId(Integer versionId) {
		return kpiDataDao.getUvByVersionId(versionId);
	}
	
}
