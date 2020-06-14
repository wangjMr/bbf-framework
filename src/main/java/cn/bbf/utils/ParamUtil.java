package cn.bbf.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ParamUtil {
	private static final List<String> EXCLUDE_FIELD_TYPE = new ArrayList<String>();

	static {
		EXCLUDE_FIELD_TYPE.add("class java.lang.String");
		EXCLUDE_FIELD_TYPE.add("class java.lang.Integer");
		EXCLUDE_FIELD_TYPE.add("class java.util.Date");
		EXCLUDE_FIELD_TYPE.add("java.lang.Boolean");
		EXCLUDE_FIELD_TYPE.add("class java.math.BigDecimal");
	}

	/**
	 * @param birthday
	 *            出生日期
	 * @param endDate
	 *            截止日期
	 * @param calculateType
	 *            计算类型 FrameConstants.STATE_AGE_YEAR 为精确到年
	 *            FrameConstants.STATE_AGE_DAY为精确到天
	 * @return 年龄
	 */
	public static Integer getAge(final Date birthday, final Date endDate, int calculateType) {
		switch (calculateType) {
		case 1: {
			return new Integer(DateUtil.beforeYears(birthday, endDate) + 1);
		}
		case 2: {
			Calendar birthdayCalendar = Calendar.getInstance();
			birthdayCalendar.setTime(birthday);
			Calendar endDateCalendar = Calendar.getInstance();
			endDateCalendar.setTime(endDate);
			if (birthdayCalendar.get(Calendar.MONTH) <= endDateCalendar.get(Calendar.MONTH)
					&& birthdayCalendar.get(Calendar.DAY_OF_MONTH) <= endDateCalendar.get(Calendar.DAY_OF_MONTH)) {
				return new Integer(DateUtil.beforeYears(birthday, endDate) + 1);
			} else {
				return new Integer(DateUtil.beforeYears(birthday, endDate));
			}
		}
		default: {
			return new Integer(DateUtil.beforeYears(birthday, endDate) + 1);
		}
		}
	}

	/**
	 * 校验身份证是否有效
	 * 
	 * @param pid
	 *            身份证号
	 * @return boolean 是否有效
	 * 
	 *         据〖中华人民共和国国家标准 GB 11643-1999〗中有关公民身份号码的规定，
	 *         公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，
	 *         三位数字顺序码和一位数字校验码。
	 * 
	 *         地址码表示编码对象常住户口所在县(市、旗、区)的行政区划代码。
	 *         生日期码表示编码对象出生的年、月、日，其中年份用四位数字表示，年、月、日之间不用分隔符。
	 *         顺序码表示同一地址码所标识的区域范围内，对同年、月、日出生的人员编定的顺序号。顺序码的奇数分给男性，偶数分给女性。
	 *         校验码是根据前面十七位数字码，按照ISO 7064:1983.MOD 11-2校验码计算出来的检验码。下面举例说明该计算方法。
	 *         15位的身份证编码首先把出生年扩展为4位，简单的就是增加一个19，但是这对于1900年出生的人不使用（这样的寿星不多了）
	 *         某男性公民身份号码本体码为34052419800101001，首先按照公式⑴计算： ∑(ai×Wi)(mod
	 *         11)……………………………………(1) 公式(1)中： i----表示号码字符从由至左包括校验码在内的位置序号；
	 *         ai----表示第i位置上的号码字符值； Wi----示第i位置上的加权因子，其数值依据公式Wi=2（n-1）(mod
	 *         11)计算得出。 i 18 17 16 15 14 13 12 11 10 9 8 7 6 5 4 3 2 1 ai 3 4 0
	 *         5 2 4 1 9 8 0 0 1 0 1 0 0 1 a1 Wi 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8
	 *         4 2 1 ai×Wi 21 36 0 25 16 16 2 9 48 0 0 9 0 5 0 0 2 a1
	 *         根据公式(1)进行计算： ∑(ai×Wi) =（21+36+0+25+16+16+2+9+48++0+0+9+0+5+0+0+2)
	 *         = 189 189 ÷ 11 = 17 + 2/11 ∑(ai×Wi)(mod 11) = 2
	 *         然后根据计算的结果，从下面的表中查出相应的校验码，其中X表示计算结果为10： ∑(ai×WI)(mod 11) 0 1 2 3 4
	 *         5 6 7 8 9 10 校验码字符值ai 1 0 X 9 8 7 6 5 4 3 2
	 *         根据上表，查出计算结果为2的校验码为所以该人员的公民身份号码应该为 34052419800101001X
	 * 
	 */
	public static boolean checkPID(String pid) {
		if (pid == null || pid.length() < 1) {
			return false;
		}
		if (!isPIDSig(pid)) {
			return false;
		}
		// 只校验18位15位不校验
		if (pid.length() == 15) {
			if (Integer.parseInt(pid.substring(8, 10)) <= 12 && Integer.parseInt(pid.substring(10, 12)) <= 31) {
				return true;
			} else {
				return false;
			}
		}
		if (pid.length() == 18) {
			return isValidateCheckBit(pid);
		}
		return false;
	}

	/**
	 * 将身份证号15位升为18位
	 * @param pid
	 *            身份证号
	 */
	public static String pid15To18(String pid) throws Exception {
		if (pid != null && pid.length() == 15 && isPIDSig(pid)) {
			StringBuffer sb = new StringBuffer(pid);
			sb.insert(6, "19");
			String checkBit = getCheckBit(sb.toString() + "0");
			sb.append(checkBit);
			return sb.toString();
		} else {
			throw new Exception("你需要转换的身份证号码错误");
		}

	}

	/**
	 * 将身份证号18位升为15位
	 * @param pid
	 *            身份证号
	 */
	public static String pid18To15(String pid) throws Exception {
		if (pid != null && pid.trim().length() == 18 && checkPID(pid)) {
			StringBuffer sb = new StringBuffer(pid.substring(0, 6) + pid.substring(8, 17));
			return sb.toString();
		} else {
			throw new Exception("你需要转换的身份证号码错误");
		}
	}

	/**
	 * 根据身份证号获取生日
	 * 
	 * @param pid
	 *            身份证号
	 */
	public static Date getBirthdayByPid(String pid) throws Exception {
		Calendar birthdayCaoendar = Calendar.getInstance();
		if (pid != null && (pid.trim().length() == 15 || pid.trim().length() == 18)) {
			if (pid.trim().length() == 15) {
				String pid18 = pid15To18(pid);
				birthdayCaoendar.set(Calendar.YEAR, Integer.parseInt(pid18.substring(6, 10)));
				// Month 值是基于 0 的。例如，0 表示 January
				birthdayCaoendar.set(Calendar.MONTH, Integer.parseInt(pid18.substring(10, 12)) - 1);
				birthdayCaoendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(pid18.substring(12, 14)));
			} else {
				birthdayCaoendar.set(Calendar.YEAR, Integer.parseInt(pid.substring(6, 10)));
				// Month 值是基于 0 的。例如，0 表示 January
				birthdayCaoendar.set(Calendar.MONTH, Integer.parseInt(pid.substring(10, 12)) - 1);
				birthdayCaoendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(pid.substring(12, 14)));
			}
			return birthdayCaoendar.getTime();
		} else {
			return null;
		}
	}

	/**
	 * 根据身份证号获取性别
	 * 
	 * @param pid
	 *            身份证号
	 * @return 性别 F为女M为男
	 */
	public static String getSexByPid(String pid) throws Exception {
		if (pid != null && (pid.trim().length() == 15 || pid.trim().length() == 18)) {
			if (pid.trim().length() == 15) {
				String pid18 = pid15To18(pid);
				if (Integer.parseInt(pid18.substring(16, 17)) % 2 == 0) {
					return "F";
				} else {
					return "M";
				}
			} else {
				if (Integer.parseInt(pid.substring(16, 17)) % 2 == 0) {
					return "F";
				} else {
					return "M";
				}
			}
		} else {
			return null;
		}
	}

	/**
	 * 校验身份证是否有效
	 */
	private static boolean isValidateCheckBit(String pid) {
		String checkBit = getCheckBit(pid);
		return pid.substring(17).equalsIgnoreCase(checkBit);
	}

	/**
	 * 校验身份证的长度和是否为数字或者X
	 */
	private static boolean isPIDSig(String str) {
		Pattern pattern = Pattern.compile("^\\d{15}|\\d{18}|(\\d{17}(x|X))$");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 计算校验位
	 */
	private static String getCheckBit(String pid) {
		String checkBit;
		String checkTable[] = new String[] { "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" };
		int[] intPid = getPIDWithNoCheckBit(pid);
		int[] wi = getWi();
		int checkBitIndex = 0;
		for (int i = 1; i < wi.length; i++) {
			checkBitIndex += wi[i] * intPid[i];
		}
		checkBitIndex %= 11;

		checkBit = checkTable[checkBitIndex];

		return checkBit;
	}

	/**
	 * 获取第i位置上的号码字符值X用10代替
	 */
	private static int[] getPIDWithNoCheckBit(String pid) {
		int intPID[] = new int[pid.length()];
		int i;
		StringBuffer pidBuf = new StringBuffer(pid);
		pid = (pidBuf.reverse()).toString();
		for (i = 0; i < pid.length(); i++) {
			if (pid.substring(i, i + 1).equalsIgnoreCase("x"))
				intPID[i] = 10;
			else
				intPID[i] = Integer.parseInt(pid.substring(i, i + 1));
		}
		return intPID;
	}

	/**
	 * 第N个位置的加权因子=2的N次方mod 11 N为0－17 方向为从右到左
	 */
	private static int[] getWi() {
		int pow = 1;
		int[] wi = new int[18];
		for (int n = 0; n < wi.length; n++) {
			wi[n] = pow % 11;
			pow *= 2;
		}
		return wi;
	}

	public static String getPinym(String a) {
		// 汉字区位码
		int li_SecPosValue[] = { 1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722,
				3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590 };
		// 存放国标一级汉字不同读音的起始区位码对应读音
		char lc_FirstLetter[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
				'S', 'T', 'W', 'X', 'Y', 'Z' };
		// 二级字库偏移量
		int ioffset = 0;
		// //存放所有国标二级汉字读音
		String ls_SecondSecTable = "CJWGNSPGCGNE[Y[BTYYZDXYKYGT[JNNJQMBSGZSCYJSYY"
				+ "[PGKBZGY[YWJKGKLJYWKPJQHY[W[DZLSGMRYPYWWCCKZNKYYGTTNJJNYKKZYTCJNMCYLQLYPYQFQRPZSLWBTGKJFYXJWZLTBNCXJJJJTXDTTSQZYCDXXHGCK"
				+ "[PHFFSS[YBGXLPPBYLL[HLXS[ZM[JHSOJNGHDZQYKLGJHSGQZHXQGKEZZWYSCSCJXYEYXADZPMDSSMZJZQJYZC[J"
				+ "[WQJBYZPXGZNZCPWHKXHQKMWFBPBYDTJZZKQHYLYGXFPTYJYYZPSZLFCHMQSHGMXXSXJ["
				+ "[DCSBBQBEFSJYHXWGZKPYLQBGLDLCCTNMAYDDKSSNGYCSGXLYZAYBNPTSDKDYLHGYMYLCXPY"
				+ "[JNDQJWXQXFYYFJLEJPZRXCCQWQQSBNKYMGPLBMJRQCFLNYMYQMSQYRBCJTHZTQFRXQHXMJJCJLXQGJMSHZKBSWYEMYLTXFSYDSWLYCJQXSJNQBSCTYHBFTDCYZDJWY"
				+ "GHQFRXWCKQKXEBPTLPXJZSRMEBWHJLBJSLYYSMDXLCLQKXLHXJRZJMFQHXHWYWSBHTRXXGLHQHFNM[YKLDYXZPYLGG[MTCFPAJJZYLJTYANJGBJPLQGDZYQY"
				+ "AXBKYSECJSZNSLYZHSXLZCGHPXZHZNYTDSBCJKDLZAYFMYDLEBBGQYZKXGLDNDNYSKJSHDLYXBCGHXYPKDJMMZNGMMCLGWZSZXZJFZNMLZZTHCSYDBDLLSCDD"
				+ "NLKJYKJSYCJLKWHQASDKNHCSGANHDAASHTCPLCPQYBSDMPJLPZJOQLCDHJJYSPRCHN[NNLHLYYQYHWZPTCZGWWMZFFJQQQQYXACLBHKDJXDGMMYDJXZLLSYGX"
				+ "GKJRYWZWYCLZMSSJZLDBYD[FCXYHLXCHYZJQ[[QAGMNYXPFRKSSBJLYXYSYGLNSCMHZWWMNZJJLXXHCHSY[[TTXRYCYXBYHCSMXJSZNPWGPXXTAYBGAJCXLY"
				+ "[DCCWZOCWKCCSBNHCPDYZNFCYYTYCKXKYBSQKKYTQQXFCWCHCYKELZQBSQYJQCCLMTHSYWHMKTLKJLYCXWHEQQHTQH[PQ"
				+ "[QSCFYMNDMGBWHWLGSLLYSDLMLXPTHMJHWLJZYHZJXHTXJLHXRSWLWZJCBXMHZQXSDZPMGFCSGLSXYMJSHXPJXWMYQKSMYPLRTHBXFTPMHYXLCHLHLZY"
				+ "LXGSSSSTCLSLDCLRPBHZHXYYFHB[GDMYCNQQWLQHJJ[YWJZYEJJDHPBLQXTQKWHLCHQXAGTLXLJXMSL[HTZKZJECXJCJNMFBY[SFYWYBJZGNYSDZSQYRSLJ"
				+ "PCLPWXSDWEJBJCBCNAYTWGMPAPCLYQPCLZXSBNMSGGFNZJJBZSFZYNDXHPLQKZCZWALSBCCJX[YZGWKYPSGXFZFCDKHJGXDLQFSGDSLQWZKXTMHSBGZMJZRGLYJ"
				+ "BPMLMSXLZJQQHZYJCZYDJWBMYKLDDPMJEGXYHYLXHLQYQHKYCWCJMYYXNATJHYCCXZPCQLBZWWYTWBQCMLPMYRJCCCXFPZNZZLJPLXXYZTZLGDLDCKLYRZZGQTG"
				+ "JHHGJLJAXFGFJZSLCFDQZLCLGJDJCSNZLLJPJQDCCLCJXMYZFTSXGCGSBRZXJQQCTZHGYQTJQQLZXJYLYLBCYAMCSTYLPDJBYREGKLZYZHLYSZQLZNWCZCLLWJQ"
				+ "JJJKDGJZOLBBZPPGLGHTGZXYGHZMYCNQSYCYHBHGXKAMTXYXNBSKYZZGJZLQJDFCJXDYGJQJJPMGWGJJJPKQSBGBMMCJSSCLPQPDXCDYYKY[CJDDYYGYWRHJRTGZ"
				+ "NYQLDKLJSZZGZQZJGDYKSHPZMTLCPWNJAFYZDJCNMWESCYGLBTZCGMSSLLYXQSXSBSJSBBSGGHFJLYPMZJNLYYWDQSHZXTYYWHMZYHYWDBXBTLMSYYYFSXJC[DXX"
				+ "LHJHF[SXZQHFZMZCZTQCXZXRTTDJHNNYZQQMNQDMMG[YDXMJGDHCDYZBFFALLZTDLTFXMXQZDNGWQDBDCZJDXBZGSQQDDJCMBKZFFXMKDMDSYYSZCMLJDSYNSBRS"
				+ "KMKMPCKLGDBQTFZSWTFGGLYPLLJZHGJ[GYPZLTCSMCNBTJBQFKTHBYZGKPBBYMTDSSXTBNPDKLEYCJNYDDYKZDDHQHSDZSCTARLLTKZLGECLLKJLQJAQNBDKKGHP"
				+ "JTZQKSECSHALQFMMGJNLYJBBTMLYZXDCJPLDLPCQDHZYCBZSCZBZMSLJFLKRZJSNFRGJHXPDHYJYBZGDLQCSEZGXLBLGYXTWMABCHECMWYJYZLLJJYHLG[DJLSLY"
				+ "GKDZPZXJYYZLWCXSZFGWYYDLYHCLJSCMBJHBLYZLYCBLYDPDQYSXQZBYTDKYXJY[CNRJMPDJGKLCLJBCTBJDDBBLBLCZQRPPXJCJLZCSHLTOLJNMDDDLNGKAQHQH"
				+ "JGYKHEZNMSHRP[QQJCHGMFPRXHJGDYCHGHLYRZQLCYQJNZSQTKQJYMSZSWLCFQQQXYFGGYPTQWLMCRNFKKFSYYLQBMQAMMMYXCTPSHCPTXXZZSMPHPSHMCLMLDQF"
				+ "YQXSZYYDYJZZHQPDSZGLSTJBCKBXYQZJSGPSXQZQZRQTBDKYXZKHHGFLBCSMDLDGDZDBLZYYCXNNCSYBZBFGLZZXSWMSCCMQNJQSBDQSJTXXMBLTXZCLZSHZCXRQ"
				+ "JGJYLXZFJPHYMZQQYDFQJJLZZNZJCDGZYGCTXMZYSCTLKPHTXHTLBJXJLXSCDQXCBBTJFQZFSLTJBTKQBXXJJLJCHCZDBZJDCZJDCPRNPQCJPFCZLCLZXZDMXMPH"
				+ "JSGZGSZZQLYLWTJPFSYASMCJBTZKYCWMYTCSJJLJCQLWZMALBXYFBPNLSFHTGJWEJJXXGLLJSTGSHJQLZFKCGNNNSZFDEQFHBSAQTGYLBXMMYGSZLDYDQMJJRGBJ"
				+ "TKGDHGKBLQKBDMBYLXWCXYTTYBKMRTJZXQJBHLMHMJJZMQASLDCYXYQDLQCAFYWYXQHZ";

		String sreturn = "";
		for (int j = 0; j < a.length(); j++) {
			String stemp = a.substring(j, j + 1);
			byte[] by = stemp.getBytes();
			if (by.length == 1) {
				sreturn = sreturn + stemp;
			} else {
				int ia = 96 + (int) by[0]; // 区码
				int ib = 96 + (int) by[1]; // 位码
				int in = ia * 100 + ib;
				if (in > 1600 && in < 5590) {
					for (int i = 0; i < 24; i++) {
						if (in < li_SecPosValue[i]) {
							sreturn = sreturn + lc_FirstLetter[i - 1];
							break;
						}
					}
				} else {
					ioffset = (ia - 56) * 94 + ib - 1;
					if (ioffset >= 0 && ioffset <= 3007) {
						sreturn = sreturn + ls_SecondSecTable.substring(ioffset, ioffset + 1);
					}
				}
			}
			sreturn = sreturn.toLowerCase();
		}
		return sreturn;
	}

	/**
	 * 把金额转换为汉字表示的数量，小数点后四舍五入保留两位
	 * 
	 */

	public static String[] chineseDigits = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

	public static String amountToChinese(double amount) {

		if (amount > 99999999999999.99 || amount < -99999999999999.99)
			throw new IllegalArgumentException("参数值超出允许范围 (-99999999999999.99 ～ 99999999999999.99)！");

		boolean negative = false;
		if (amount < 0) {
			negative = true;
			amount = amount * (-1);
		}

		long temp = Math.round(amount * 100);
		int numFen = (int) (temp % 10); // 分
		temp = temp / 10;
		int numJiao = (int) (temp % 10); // 角
		temp = temp / 10;
		// temp 目前是金额的整数部分

		int[] parts = new int[20]; // 其中的元素是把原来金额整数部分分割为值在 0~9999 之间的数的各个部分
		int numParts = 0; // 记录把原来金额整数部分分割为了几个部分（每部分都在 0~9999 之间）
		for (int i = 0;; i++) {
			if (temp == 0)
				break;
			int part = (int) (temp % 10000);
			parts[i] = part;
			numParts++;
			temp = temp / 10000;
		}

		boolean beforeWanIsZero = true; // 标志“万”下面一级是不是 0

		String chineseStr = "";
		for (int i = 0; i < numParts; i++) {

			String partChinese = partTranslate(parts[i]);
			if (i % 2 == 0) {
				if ("".equals(partChinese))
					beforeWanIsZero = true;
				else
					beforeWanIsZero = false;
			}

			if (i != 0) {
				if (i % 2 == 0)
					chineseStr = "亿" + chineseStr;
				else {
					if ("".equals(partChinese) && !beforeWanIsZero) // 如果“万”对应的
						// part 为
						// 0，而“万”下面一级不为
						// 0，则不加“万”，而加“零”
						chineseStr = "零" + chineseStr;
					else {
						if (parts[i - 1] < 1000 && parts[i - 1] > 0) // 如果"万"的部分不为
							// 0,
							// 而"万"前面的部分小于
							// 1000
							// 大于 0，
							// 则万后面应该跟“零”
							chineseStr = "零" + chineseStr;
						chineseStr = "万" + chineseStr;
					}
				}
			}
			chineseStr = partChinese + chineseStr;
		}

		if ("".equals(chineseStr)) // 整数部分为 0, 则表达为"零元"
			chineseStr = chineseDigits[0];
		else if (negative) // 整数部分不为 0, 并且原金额为负数
			chineseStr = "负" + chineseStr;

		chineseStr = chineseStr + "元";

		if (numFen == 0 && numJiao == 0) {
			chineseStr = chineseStr + "整";
		} else if (numFen == 0) { // 0 分，角数不为 0
			chineseStr = chineseStr + chineseDigits[numJiao] + "角";
		} else { // “分”数不为 0
			if (numJiao == 0)
				chineseStr = chineseStr + "零" + chineseDigits[numFen] + "分";
			else
				chineseStr = chineseStr + chineseDigits[numJiao] + "角" + chineseDigits[numFen] + "分";
		}

		return chineseStr;

	}

	/**
	 * 把一个 0~9999 之间的整数转换为汉字的字符串，如果是 0 则返回 ""
	 * 
	 */
	private static String partTranslate(int amountPart) {

		if (amountPart < 0 || amountPart > 10000) {
			throw new IllegalArgumentException("参数必须是大于等于 0，小于 10000 的整数！");
		}

		String[] units = new String[] { "", "拾", "佰", "仟" };

		int temp = amountPart;

		String amountStr = new Integer(amountPart).toString();
		int amountStrLength = amountStr.length();
		boolean lastIsZero = true; // 在从低位往高位循环时，记录上一位数字是不是 0
		String chineseStr = "";

		for (int i = 0; i < amountStrLength; i++) {
			if (temp == 0) // 高位已无数据
				break;
			int digit = temp % 10;
			if (digit == 0) { // 取到的数字为 0
				if (!lastIsZero) // 前一个数字不是 0，则在当前汉字串前加“零”字;
					chineseStr = "零" + chineseStr;
				lastIsZero = true;
			} else { // 取到的数字不是 0
				chineseStr = chineseDigits[digit] + units[i] + chineseStr;
				lastIsZero = false;
			}
			temp = temp / 10;
		}
		return chineseStr;
	}

	public static String convertDateStrToString(String date) {
		StringBuffer dateStr = new StringBuffer();
		if (!StringUtils.isBlank(date)) {
			dateStr.append(date.substring(0, 4));
			dateStr.append(date.substring(5, 7));
			dateStr.append(date.substring(8, 10));

		}
		return dateStr.toString();

	}

	/**
	 *
	 * 拷贝属性相同的方法，不拷贝复杂对象
	 *
	 */
	public static void copy(Object destObj, Object sourceBoj) {
		Field[] destFields = destObj.getClass().getDeclaredFields();
		Field[] sourceFields = sourceBoj.getClass().getDeclaredFields();
		String destFieldName = null;
		String sourceFieldName = null;
		for (Field sourceField : sourceFields) {
			sourceFieldName = sourceField.getName();
			if (sourceFieldName.equals("serialVersionUID")) {
				continue;
			}
			if (!EXCLUDE_FIELD_TYPE.contains(sourceField.getType().toString())) {
				continue;
			}
			for (Field destField : destFields) {
				destFieldName = destField.getName();
				if (destFieldName.equals(sourceFieldName)) {
					Reflections.setFieldValue(destObj, destFieldName,
							Reflections.getFieldValue(sourceBoj, sourceFieldName));
					break;
				}
			}
		}

	}
	
	public static void copyMap(Map<String, Object> destObj, Map<String, Object> sourceBoj) {
		for(String soKey : sourceBoj.keySet()) {
			destObj.put(soKey, sourceBoj.get(soKey));
		}
	}
	
	/**
	 *
	 * 拷贝属性相同的方法，不拷贝复杂对象，增加排除的属性
	 *
	 */
	public static void copy(Object destObj, Object sourceBoj, String[] excludeProperties) {
		Field[] destFields = destObj.getClass().getDeclaredFields();
		Field[] sourceFields = sourceBoj.getClass().getDeclaredFields();
		String destFieldName = null;
		String sourceFieldName = null;
		label: for (Field sourceField : sourceFields) {
			sourceFieldName = sourceField.getName();
			if (sourceFieldName.equals("serialVersionUID")) {
				continue;
			}
			if (!EXCLUDE_FIELD_TYPE.contains(sourceField.getType().toString())) {
				continue;
			}

			for (String pro : excludeProperties) {
				if (sourceFieldName.equals(pro)) {
					continue label;
				}
			}
			for (Field destField : destFields) {
				destFieldName = destField.getName();
				if (destFieldName.equals(sourceFieldName)) {
					Reflections.setFieldValue(destObj, destFieldName,
							Reflections.getFieldValue(sourceBoj, sourceFieldName));
					break;
				}
			}
		}
	}

}
