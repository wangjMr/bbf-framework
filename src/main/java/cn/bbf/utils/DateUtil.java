package cn.bbf.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * yyyy-MM-dd
	 */
	public static final String YEAR_MONTH_DAY_PATTERN = "yyyy-MM-dd";

	/**
	 * yyyyMMdd
	 */
	public static final String YEARMONTHDAY = "yyyyMMdd";

	/**
	 * HH:mm:ss
	 */
	public static final String HOUR_MINUTE_SECOND_PATTERN = "HH:mm:ss";

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String YMDHMS_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 根据传入的日期格式化pattern将传入的日期格式化成字符串。
	 * 
	 * @param date
	 *            要格式化的日期对象
	 * @param pattern
	 *            日期格式化pattern
	 * @return 格式化后的日期字符串
	 */
	public static String format(final Date date, final String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		if (date == null)
			return null;
		return df.format(date);
	}

	/**
	 * 返回给定的beforeDate比afterDate早的年数。如果beforeDate晚于afterDate，则 返回负数。
	 * 
	 * @param beforeDate
	 *            要比较的早的日期
	 * @param afterDate
	 *            要比较的晚的日期
	 * @return beforeDate比afterDate早的年数，负数表示晚。
	 */
	public static int beforeYears(final Date beforeDate, final Date afterDate) {
		Calendar beforeCalendar = Calendar.getInstance();
		beforeCalendar.setTime(beforeDate);
		beforeCalendar.set(Calendar.MONTH, 1);
		beforeCalendar.set(Calendar.DATE, 1);
		beforeCalendar.set(Calendar.HOUR, 0);
		beforeCalendar.set(Calendar.SECOND, 0);
		beforeCalendar.set(Calendar.MINUTE, 0);
		Calendar afterCalendar = Calendar.getInstance();
		afterCalendar.setTime(afterDate);
		afterCalendar.set(Calendar.MONTH, 1);
		afterCalendar.set(Calendar.DATE, 1);
		afterCalendar.set(Calendar.HOUR, 0);
		afterCalendar.set(Calendar.SECOND, 0);
		afterCalendar.set(Calendar.MINUTE, 0);
		boolean positive = true;
		if (beforeDate.after(afterDate))
			positive = false;
		int beforeYears = 0;
		while (true) {
			boolean yearEqual = beforeCalendar.get(Calendar.YEAR) == afterCalendar.get(Calendar.YEAR);
			if (yearEqual) {
				break;
			} else {
				if (positive) {
					beforeYears++;
					beforeCalendar.add(Calendar.YEAR, 1);
				} else {
					beforeYears--;
					beforeCalendar.add(Calendar.YEAR, -1);
				}
			}
		}
		return beforeYears;
	}

	/**
	 * 日期加数字天数之后得到新的日期
	 * 
	 * @param d
	 *            传进来的日期参数 eg:Thu Dec 16 15:19:25 CST 2010。
	 * @param day
	 *            天数 eg:12
	 * @return eg:Tue Dec 28 15:19:25 CST 2010。
	 */
	public static Date addDate(Date d, long day) {

		long time = d.getTime();
		day = day * 24 * 60 * 60 * 1000;
		time += day;
		return new Date(time);
	}

	/**
	 * 
	 * @since 2017年10月17日
	 */
	public static Date minusDate(Date d, long day) {

		long time = d.getTime();
		day = day * 24 * 60 * 60 * 1000;
		time -= day;
		return new Date(time);
	}

	/**
	 * 将日期String转换为java.util.Calendar
	 * 
	 * @return calendar
	 */
	public static Calendar toCalendar(String str) {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		return calendar;
	}

	/**
	 *
	 * 获取天数
	 *
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 天数
	 */
	public static int getDays(Date beginDate, Date endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(YEAR_MONTH_DAY_PATTERN);
		String sBeginDate = sdf.format(beginDate);
		String sEndDate = sdf.format(endDate);
		int times = 0;
		try {
			times = (int) ((sdf.parse(sEndDate).getTime() - sdf.parse(sBeginDate).getTime()) / (1000 * 60 * 60 * 24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return times;
	}

	public static boolean afterOneDay(Date oneDate,Date compDate) {
		if (CommonUtils.isBlankOrEmpty(oneDate) || CommonUtils.isBlankOrEmpty(compDate)) {
			return false;
		}
		if(compDate.after(oneDate)) {
			return true;
		}
		String oneDateStr = format(oneDate, YEARMONTHDAY);
		String compDateStr = format(compDate, YEARMONTHDAY);
		if(oneDateStr.equals(compDateStr)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取某年第一天日期
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */
	public static Date getCurrYearFirst(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	/**
	 * 获取某年最后一天日期
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */
	public static Date getCurrYearLast(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();
		return currYearLast;
	}

	/**
	 *
	 * 计算儿童月龄
	 *
	 */
	public static Integer getMonthAgeByBirthday(Date birthday) {
		if (birthday == null) {
			return 0;
		}
		if (birthday.after(new Date())) {
			return 0;
		}
		Calendar now = Calendar.getInstance();
		Integer nowYear = now.get(Calendar.YEAR);
		Integer nowMonth = now.get(Calendar.MONTH);
		Calendar birthCal = Calendar.getInstance();
		birthCal.setTime(birthday);
		Integer birthYear = birthCal.get(Calendar.YEAR);
		Integer birthMonth = birthCal.get(Calendar.MONTH);
		Integer age = nowYear - birthYear;
		if (nowYear > birthYear && nowMonth < birthMonth) {
			age--;
		}
		return age;
	}

	/**
	 * 
	 * @author AlphGo
	 * @since 2017年1月12日 下午1:28:25
	 * 当前季度开始时间
	 *
	 */
	public static Date getQuarterStartTime(int year, int season) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = null;
		try {
			if (season == 1)
				c.set(Calendar.MONTH, 0);
			else if (season == 2)
				c.set(Calendar.MONTH, 3);
			else if (season == 3)
				c.set(Calendar.MONTH, 6);
			else if (season == 4)
				c.set(Calendar.MONTH, 9);
			c.set(Calendar.DATE, 1);
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}
	

	/**
	 * 当前季度的结束时间，
	 * 
	 */
	public static Date getQuarterEndTime(int year, int season) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getQuarterStartTime(year, season));
		cal.add(Calendar.MONTH, 3);
		return addDate(cal.getTime(),-1);
	}

	/**
	 *
	 * 获取一年以后日期
	 *
	 */
	public static Date getYearAfterDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - 1);
		return cal.getTime();
	}

	/**
	 * 根据月份返回相应季度
	 */
	public static Integer getThisSeasonTime(int month) {
		int quarter = 0;
		if (month >= 1 && month <= 3) {
			quarter = 1;
		}
		if (month >= 4 && month <= 6) {
			quarter = 2;
		}
		if (month >= 7 && month <= 9) {
			quarter = 3;
		}
		if (month >= 10 && month <= 12) {
			quarter = 4;
		}
		return quarter;
	}

	/**
	 * 根据系统时间返回当月第一天
	 */
	public static String getMonthOneDay(Calendar c) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String first = format.format(c.getTime());
		return first;
	}

	/**
	 * 根据系统时间返回当月最后一天
	 */
	public static String getMonthLastDay(Calendar ca) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String last = format.format(ca.getTime());
		return last;
	}

	/**
	 * 获得该月第一天
	 *
	 */
	public static String getFirstDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最小天数
		int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String firstDayOfMonth = sdf.format(cal.getTime());
		return firstDayOfMonth;
	}

	/**
	 * 获得该月最后一天
	 *
	 */
	public static String getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastDayOfMonth = sdf.format(cal.getTime());
		return lastDayOfMonth;
	}

	/**
	 * 获取前/后半年的开始时间
	 * 
	 */
	public static Date getHalfYearStartTime(Date now) {
		SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 6) {
				c.set(Calendar.MONTH, 0);
			} else if (currentMonth >= 7 && currentMonth <= 12) {
				c.set(Calendar.MONTH, 6);
			}
			c.set(Calendar.DATE, 1);
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;

	}

	/**
	 * 获取前/后半年的结束时间
	 * 
	 */
	public static Date getHalfYearEndTime(Date now) {
		SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		now = null;
		try {
			if (currentMonth >= 1 && currentMonth <= 6) {
				c.set(Calendar.MONTH, 5);
				c.set(Calendar.DATE, 30);
			} else if (currentMonth >= 7 && currentMonth <= 12) {
				c.set(Calendar.MONTH, 11);
				c.set(Calendar.DATE, 31);
			}
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 判断日期是否一致。
	 * 
	 * @param date
	 *            要格式化的日期对象
	 * @return 格式化后的日期字符串
	 */
	public static boolean dateIsQuite(Date date, Date newDate) {
		SimpleDateFormat df = new SimpleDateFormat(YEAR_MONTH_DAY_PATTERN);
		if (date == null || newDate == null) {
			return true;
		}
		String d1 = df.format(date);
		String d2 = df.format(newDate);
		if (d1.equals(d2)) {
			return true;
		}
		return false;
	}
	
	/**
     * 判断时间是否在时间段内
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date lastTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(lastTime);
		if (dateIsQuite(nowTime, beginTime)) {
			return true;
		}
        if (date.after(end) && date.before(begin)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 获取传入的天数得到日期
     * @param day
     */
    public static String  addDate(Integer day){
    	Date today = new Date();  
    	Calendar c = Calendar.getInstance();  
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, day);
        Date tomorrow = c.getTime();
        String dateStr = format(tomorrow,YEAR_MONTH_DAY_PATTERN);
		return dateStr;
    }

    
}
