package com.web.abt.m.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateFormatUtil {

    public static String PATTERN_DATE = "yyyy-MM-dd";
    public static String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将javaDate类型的日期转化为 yyyy-MM-dd HH:mm:ss 格式的字符串
     * 
     * @param date
     * @return
     */
    public static String dateToString(Date date) {

        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_DATETIME);
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 将javaDate类型的日期转化为自定义格式的字符串
     * 
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date, String format) {

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 将字符串格式的时间转化为Date类型的日期，
     * 
     * @param dateString
     * @param format
     * @return
     */
    public static Date stringToDate(String dateString, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(dateString);
            return date;
        }
        catch (ParseException e) {
            
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前日期多少天之后的日期
     * 
     * @param days
     * @return
     */
    public static Date getNextDayByCurrent(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * 获取当前日期多少天之前的日期
     * 
     * @param days
     * @return
     */
    public static Date getBeforeDayByCurrent(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        return calendar.getTime();
    }

    /**
     * 获取当前日期多少分钟之后的日期
     * 
     * @param days
     * @return
     */
    public static Date getNextTimeByDate(Date date, int minuts) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minuts);
        return calendar.getTime();
    }

    /**
     * 获取当前日期多少分钟之后的日期
     * 
     * @param days
     * @return
     */
    public static Date getNextTimeBySecond(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }
    
    /**
     * 获取当前日期多少分钟之后的日期
     * 
     * @param days
     * @return
     */
    public static Date getNextTimeByMinute(Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 计算两个日期的天差
     * 
     * @param beforDate
     * @param afterDate
     * @return
     */
    public static Long getDayBetweenDate(Date beforDate, Date afterDate) {
        return (afterDate.getTime() - beforDate.getTime()) / (1000 * 60 * 60 * 24);
    }

    /**
     * 计算两个日期的时差
     * 
     * @param beforDate
     * @param afterDate
     * @return
     */
    public static Long getHourBetweenDate(Date beforDate, Date afterDate) {
        return (afterDate.getTime() - beforDate.getTime()) / (1000 * 60 * 60);
    }

    /**
     * 计算两个日期的分差
     * 
     * @param beforDate
     * @param afterDate
     * @return
     */
    public static Long getMinuteBetweenDate(Date beforDate, Date afterDate) {
        return (afterDate.getTime() - beforDate.getTime()) / (1000 * 60);
    }

    /**
     * 计算两个日期的秒差
     * 
     * @param beforDate
     * @param afterDate
     * @return
     */
    public static Long getSecondBetweenDate(Date beforDate, Date afterDate) {
        return (afterDate.getTime() - beforDate.getTime()) / (1000);
    }

    public static String getCurrenYearAndWeek() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR); // 获取年
        int week = c.get(Calendar.WEEK_OF_YEAR);// 获取当前的周
        if (week < 10) {
            return year + "0" + c.get(Calendar.WEEK_OF_YEAR);
        } else {
            return year + "" + c.get(Calendar.WEEK_OF_YEAR);
        }
    }

    /**
     * 获取当前日期
     * 
     * @return
     */
    public static String getCurrentDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取当前日期
     * 
     * @return
     */
    public static String getCurrentDayStart() {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_DATE);
        String beginDate = formatter.format(new Date(System.currentTimeMillis()));
        return beginDate + " 00:00:00";
    }

    /**
     * 获取日期的当天开始时间
     * 
     * @return
     */
    public static Date getDayStart(Date day) {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_DATE);
        String beginDate = formatter.format(day);
        try {
            Date date = formatter.parse(beginDate);
            return date;
        }
        catch (ParseException e) {
            
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取日期的当天结束时间
     * 
     * @return
     */
    public static Date getDayEnd(Date day) {
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_DATETIME);
        try {
            Date date = format.parse(getDayEndStr(day));
            return date;
        }
        catch (ParseException e) {
            
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取日期的当天开始时间字符串
     * 
     * @return
     */
    public static String getDayStartStr(Date day) {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_DATE);
        String beginDate = formatter.format(day);
        return beginDate + " 00:00:00";
    }

    public static Date getHourStart(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static Date getNextTimeByHour(Date date, int hrs) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, hrs);
        return c.getTime();
    }

    public static Date getNextTimeByHour(Date date, double hrs) {
    	int hours = (int) hrs;
    	int mins = (int) ((hrs - hours) * 60);
        Calendar c = Calendar.getInstance();
    	c.setTime(date);
        if(hours > 0){
            c.add(Calendar.HOUR_OF_DAY, hours);
        }
        if(mins > 0){
            c.add(Calendar.MINUTE, mins);
        }
        return c.getTime();
    }
    /**
     * 获取时间的当天开始时间字符串
     * 
     * @return
     */
    public static String getBeforTime(int hour) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, -hour);
        return dateToString(c.getTime());
    }

    /**
     * 获取日期的当天结束时间字符串
     * 
     * @return
     */
    public static String getDayEndStr(Date day) {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_DATE);
        String endDate = formatter.format(day);
        return endDate + " 23:59:59";
    }

    /**
     * 获取距离当天的截止时间还有多少秒
     * 
     * @return
     */
    public static int getTodayZero() {
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        time = calendar.getTimeInMillis() - time;
        return (int) (time / 1000);// 距离第二天还有多少秒
    }

    /**
     * 获取昨天的日期格式 09-25
     * 
     * @param days
     * @return
     */
    public static String getYesterDayFormart() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        return formatter.format(calendar.getTime());
    }
    
    public static List<String> getHalfHourBetweenTimeRange(Date fromTime, double hours) {
        Date endTime = getNextTimeByHour(fromTime, hours);
        if(!endTime.equals(fromTime)){
        	return null;
        }
    	
        List<String> result = new ArrayList<String>();
        Date calTime = fromTime;
        do{
        	result.add(dateToString(calTime, "HHmm"));
        	calTime = getNextTimeByMinute(calTime, 30);
        }while(endTime.after(calTime));
        
        return result;
    }

    public static void main(String args[]) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	String strDate = "2014-12-12 17:00";
    	try {
			Date date=sdf.parse(strDate);
	    	System.out.print(date.getTime());
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
    }
}
