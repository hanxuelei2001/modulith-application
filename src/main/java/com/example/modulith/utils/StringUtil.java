package com.example.modulith.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.buf.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringUtil {

    public static final String COMMA = ",";

    public static final String SPACE = " ";

    public static final String SEMICOLON = ";";

    public static final String BLANK = "\\s+";

    private static final Pattern HTML_PATTERN = Pattern.compile(
            "(<!DOCTYPE html>)|(<[a-z]+\\b[^>]*>)|(</[a-z]+>)|(<[a-z]+\\b[^>]*/>)",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * 句子的结束符号
     */
    private static final List<Character> END_SYMBOL = new ArrayList<Character>() {{
        add('.');
        add('!');
        add('?');
        add('。');
        add('？');
        add('！');
    }};

    /**
     * 句子分隔的符号
     */
    private static final List<Character> SEPARATOR_SYMBOL = new ArrayList<Character>() {{
        add(',');
        add('，');
    }};

    /**
     * 截取完整句子
     * 优先截取完成的句子，如果没有完成的句子，截取半句话。
     * 如果没有半句话，直接截取完整的单词。如果还是没有，直接截取指定数量的字符
     *
     * @param text      文本
     * @param maxLength 最大长度
     * @return 截取后的文本
     */
    public static String subCompletedSentence(String text, int maxLength) {
        // 如果 maxLength 小于 0，抛出异常
        if (maxLength < 0) {
            throw new StringIndexOutOfBoundsException("maxLength must be greater than 0");
        }
        // 如果 maxLength 等于 0 或文本是空的，直接返回空字符串
        if (maxLength == 0 || text == null) {
            return "";
        }
        // 如果文本长度小于等于 maxLength，直接返回文本
        if (text.length() <= maxLength) {
            return text;
        }
        // 截取的下标索引
        int endIndex = -1;
        // 次级的结束索引
        int subEndIndex = -1;
        // 单次通过查找最后一个标点符号来截取
        for (int i = maxLength; i > 0; i--) {
            char c = text.charAt(i);
            // 如果有整句的符号，则结束查找
            if (END_SYMBOL.contains(c)) {
                endIndex = i + 1;
                break;
            }
            // 记录最后一个分隔的符号位置
            if (subEndIndex == -1 && SEPARATOR_SYMBOL.contains(c)) {
                subEndIndex = i;
            }
        }
        // 如果没有找到整句的符号，且找到分隔的符号，则按照分隔的符号截取
        if (endIndex == -1 && subEndIndex != -1) {
            endIndex = subEndIndex;
        }
        // 如果没有找到标点符号，查找最后一个空格
        if (endIndex == -1) {
            int lastIndexOf = text.lastIndexOf(' ', maxLength + 1);
            if (lastIndexOf > 0) {
                endIndex = lastIndexOf;
            }
        }
        // 如果还是没有找到，直接截取
        if (endIndex == -1) {
            endIndex = maxLength;
        }
        return text.substring(0, endIndex);
    }

    /**
     * 判断字符串是否为NULL或空字符串
     *
     * @param s
     * @return
     */
    public static boolean isEmptyOrBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * 去除字符串中除了字母数字以外的标点符号
     *
     * @param str
     * @return
     */
    public static String replaceStrBlankAndSymbol(String str) {
        char[] strs = str.toCharArray();
        String newStr = "";
        for (int i = 0; i < strs.length; i++) {
            if (Character.isLetter(strs[i]) || Character.isDigit(strs[i])) {
                newStr += strs[i];
            }
        }
        return newStr;
    }

    /**
     * 判断字符串是否是邮箱
     *
     * @param s
     * @return
     */
    public static boolean isEmail(String s) {
        if (s == null || s.trim().isEmpty())
            return false;
        s = s.trim();
        Pattern p = Pattern.compile("^[a-zA-Z0-9'_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 判断字符串是否是手机号
     *
     * @param s
     * @return
     */
    public static boolean isPhone(String s) {
        if (s == null || s.trim().isEmpty())
            return false;
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public static String removeEmojis(String s) {
        if (isEmptyOrBlank(s))
            return "";
        return s.replaceAll("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", "");
    }

    /**
     * 移除字符串中的特殊字符
     * @param s 要处理的字符串
     * @return 处理结果
     */
    public static String removeSpecialChar(String s) {
        if (isEmptyOrBlank(s)) {
            return s;
        }
        return s.replaceAll("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]", "");
    }

    public static String convertCNStrToString(List<String> str) {
        if (str == null || str.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder();
        for (String id : str) {
            sb.append("N'").append(id.trim()).append("'").append(",");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 转换为对应数量 SQL 参数化语句
     *
     * @param size 参数数量
     * @return SQL 参数化语句
     */
    public static String generateParamSql(int size) {
        if (size <= 0) {
            return "";
        }
        return String.join(",", Collections.nCopies(size, "?"));
    }


    /**
     * 将id list转换为字符串
     * <p>
     * 如：'001','002','003'
     *
     * @param ids
     * @return zjj 2016.6.14
     */
    public static String convertIdsToString(List<String> ids) {
//        if (ids == null || ids.isEmpty()){
        if (ids == null || ids.stream().filter(id -> id != null).collect(Collectors.toList()).isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String id : ids) {
            if (id != null) {
                sb.append(formatParameter(id.trim(), false)).append(",");
            }
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 高效将 ids 集合转换为字符串
     *
     * @param stream
     * @return
     */
    public static String convertIdsToString(Stream<String> stream) {
        return stream.filter(Objects::nonNull).map(x -> formatParameter(x, false)).collect(Collectors.joining(","));
    }

    /**
     * 高效将 ids 集合转换为字符串
     *
     * @return
     */
    public static String convertNamesToString(List<String> names) {
        List<String> namesTemp = new ArrayList<>();
        for (String name : names) {
            if (name.trim().contains("'")) {
                namesTemp.add(name.trim().replace("'", "''"));
            } else {
                namesTemp.add(name);
            }
        }
        String subString = String.join("',N'", namesTemp);
        return "N'" + subString + "'";
    }

    public static String toLowerWithoutSpace(String string) {
        return string == null ? null : string.toLowerCase().replace(" ", "");
    }

    /**
     * 组合字符串
     * 用空格分隔
     *
     * @param strs
     * @return
     */
    public static String combineWithSpace(String... strs) {
        return combineByChar(' ', strs);
    }

    /**
     * 组合字符串
     * 用下划线分隔
     *
     * @param strs
     * @return
     */
    public static String combineWithUnderline(String... strs) {
        return combineByChar('_', strs);
    }

    /**
     * 用指定的字符组合字符串
     *
     * @param c
     * @param stars
     * @return
     */
    public static String combineByChar(char c, String... stars) {
        String result = "";
        for (String str : stars) {
            if (isEmptyOrBlank(str))
                continue;
            if (result == "")
                result += str.trim();
            else
                result += c + str.trim();
        }
        return result.trim();
    }

    private static final String[] UPPER_LETTERS = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static String convertNumToUpperLetter(int num) {
        if (num < 0) {
            return "Z";
        }
        num = num - 1;
        if (num < UPPER_LETTERS.length) {
            return UPPER_LETTERS[num];
        }
        int index1 = num / UPPER_LETTERS.length;
        int index2 = num % UPPER_LETTERS.length;
        return UPPER_LETTERS[index1 - 1] + UPPER_LETTERS[index2];
    }

    /**
     * 移除字符串中的数字
     *
     * @param str
     * @return
     */
    public static String removeDigits(String str) {
        if (str == null)
            return null;
        return str.replaceAll("\\d", "");
    }

    public static String removeNoDigits(String str) {
        if (str == null)
            return null;
        return str.replaceAll("[^0-9]", "");
    }

    public static String removeSymbol(String str) {
        if (str == null)
            return null;
        return str.replaceAll("[^^0-9a-zA-Z]", "");
    }

    public static String getEndDigits(String str) {
        if (str == null)
            return "";
        //以数字结尾的
        if (!str.matches(".*\\d+$"))
            return "";
        //替换掉结尾数字之前的
        return str.replaceAll(".*[^\\d](?=(\\d+))", "");
    }

    public static boolean isAContainsB(String a, String b) {
        a = a.trim().toUpperCase();
        b = b.trim().toUpperCase();
        return a.contains(b);
    }

    /**
     * 是否包含指定正则匹配的内容
     *
     * @param str 字符串
     * @param regex 正则
     * @return 是否包含
     */
    public static boolean isContainsRegexIgnoreCase(String str, String regex) {
        if (isEmptyOrBlank(str) || isEmptyOrBlank(regex)) {
            return false;
        }
        // 忽略大小写
        String finalRegex = "(?i)" + regex;
        Pattern p = Pattern.compile(finalRegex);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static String removeSpacesToOne(String str) {
        str = str.trim();
        Pattern p = Pattern.compile("\\s+");
        Matcher m = p.matcher(str);
        return m.replaceAll(" ");
    }

    /**
     * 判断传来的字符串是否存在集合中
     *
     * @param value
     * @param values
     * @return
     */
    public static boolean isExistInArray(String value, List<String> values) {
        if (StringUtil.isEmptyOrBlank(value))
            return false;
        for (String v : values) {
            if (StringUtil.isEmptyOrBlank(v))
                continue;
            if (v.trim().equalsIgnoreCase(value.trim()))
                return true;
        }
        return false;
    }

    /**
     * 合并两个字符串数组
     *
     * @param fromArray
     * @param toArray
     * @return
     */
    public static List<String> mergeStringArray(List<String> fromArray, List<String> toArray) {
        for (String from : fromArray) {
            if (isEmptyOrBlank(from))
                continue;
            from = from.trim();

            boolean isExist = false;
            for (String to : toArray) {
                if (isEmptyOrBlank(to))
                    continue;
                to = to.trim();

                if (from.equalsIgnoreCase(to)) {
                    isExist = true;
                    break;
                }
            }

            if (!isExist)
                toArray.add(from);
        }

        return toArray;
    }

    /**
     * 检查字符串是否大写字母开头
     *
     * @param value
     * @return
     */
    public static boolean isBeginWithCapital(String value) {
        if (isEmptyOrBlank(value))
            return false;
        value = value.trim();
        char first = value.charAt(0);
        return Character.isUpperCase(first);
    }

    /**
     * Long 类型转换为 int
     * @param num Long 类型值
     * @return 转换结果
     */
    public static int paresLong(Long num) {
        return num == null ? 0 : Math.toIntExact(num);
    }

    private static final char[] FILE_Illegal_CHARS = {'/', '\'', '"', '?', ':', '<', '>', '*', '|', '%', '#', '\\'};


    /**
     * 去掉文件名中的非法字符
     *
     * @param name
     * @return
     */
    public static String excapeFileName(String name) {
        if (isEmptyOrBlank(name))
            return name;
        for (char c : FILE_Illegal_CHARS) {
            name = name.replace(c, '-');
        }
        return name;
    }

    public static String httpsToHttp(String url) {
        if (StringUtil.isEmptyOrBlank(url))
            return "";
        return url;
    }

    public static boolean equalsIgnoreCaseWithoutSpace(String str1, String str2) {
        if (isEmptyOrBlank(str1) && isEmptyOrBlank(str2))
            return true;
        if (isEmptyOrBlank(str1) || isEmptyOrBlank(str2))
            return false;
        return str1.replace(" ", "").equalsIgnoreCase(str2.replace(" ", ""));
    }

    public static String formatString(String value, Set<String> values) {
        if (isEmptyOrBlank(value) || values.isEmpty())
            return null;
        for (String v : values) {
            if (equalsIgnoreCaseWithoutSpace(value, v))
                return v;
        }
        return null;
    }

    public static Set<String> getMatchString(String str, String regex) {
        Set<String> result = new HashSet<>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find())
            result.add(m.group());
        return result;
    }




    public static String formatAttr(String value) {
        if (isEmptyOrBlank(value))
            return value;
        value = value.trim();

        if ("State Infant/Toddler Program".equalsIgnoreCase(value) || "State Preschool Program".equalsIgnoreCase(value))
            return "State Program";

        return value;
    }

    public static boolean isUUID(String value) {
        if (isEmptyOrBlank(value))
            return false;
        try {
            UUID.fromString(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isTrue(String value) {
        if (StringUtil.isEmptyOrBlank(value)) {
            return false;
        }
        // 拿到的 value 有可能是 yes,yes
        // 由于导入出现的问题导致的,后续会对导入进行解决
        // 对数据进行去重, 如果数据是大于 1 的,那么可能出现了未知错误,这个先不处理
        List<String> valueList = Arrays.stream(value.split(";")).map(String::trim).distinct().collect(Collectors.toList());
        if (valueList.size() == 1) {
            return "true".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value);
        }
        return false;
    }

    private static final String[] SPECIAL_CHARS = {" ", " ", "　"};

    /**
     * 判断两个字符是否相等，忽略大小写和空白字符
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equalsIgnoreCaseAndSpecialChar(String str1, String str2) {
        if (isEmptyOrBlank(str1) && isEmptyOrBlank(str2))
            return true;
        if (isEmptyOrBlank(str1) || isEmptyOrBlank(str2))
            return false;
        for (String specialChar : SPECIAL_CHARS) {
            str1 = str1.replace(specialChar, "");
            str2 = str2.replace(specialChar, "");
        }
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * 将id list转换为字符串
     * <p>
     * 如：001,002,003
     *
     * @param ids
     * @return
     */
    public static String convertIdsToStringWithoutQuotation(List<String> ids) {
        if (ids == null || ids.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder();
        for (String id : ids) {
            sb.append(id.trim()).append(",");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    private static final String[] honorifics = new String[]{"Miss.", "Miss", "Miss .", "Ms.", "Mr.", "Mrs."};

    /**
     * 去除姓名中的Miss等前缀
     *
     * @param displayName
     * @return
     */
    public static String removeHonorific(String displayName) {
        if (StringUtil.isEmptyOrBlank(displayName)) {
            return displayName;
        }
        for (String honorific : honorifics) {
            displayName = displayName.replace(honorific, "");
        }
        return displayName.trim();
    }

    /**
     * 根据HttpServletRequest 解析ip地址
     *
     * @param request 请求
     */
    public static String parseIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtil.isEmptyOrBlank(ip))
            ip = request.getHeader("Proxy-Client-IP");
        if (StringUtil.isEmptyOrBlank(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtil.isEmptyOrBlank(ip))
            ip = request.getHeader("HTTP_CLIENT_IP");
        if (StringUtil.isEmptyOrBlank(ip))
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (StringUtil.isEmptyOrBlank(ip))
            ip = request.getRemoteAddr();
        return ip;
    }

    /**
     * 去重以逗号分割的ids
     *
     * @param ids
     * @return
     */
    public static String filterDuplicateIds(String ids) {
        if (StringUtil.isEmptyOrBlank(ids)) {
            return ids;
        }
        List<String> results = new ArrayList<>();
        for (String id : ids.split(",")) {
            if (StringUtil.isEmptyOrBlank(id)) {
                continue;
            }
            id = id.trim();
            if (results.contains(id)) {
                continue;
            }
            results.add(id);
        }
        return StringUtils.join(results, ',');
    }

    public static String convertLikeParam(String value) {
        if (StringUtil.isEmptyOrBlank(value)) {
            return "%%";
        }
        return "%" + value + "%";
    }

    /**
     * 将字符串转为数字，如果无法转换，则返回NULL
     *
     * @param value 需要转换的字符串
     * @return 转换结果
     */
    public static Integer tryParseInteger(String value) {
        if (StringUtil.isEmptyOrBlank(value)) {
            return null;
        }
        value = value.trim();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double tryParseDouble(String value) {
        if (StringUtil.isEmptyOrBlank(value)) {
            return null;
        }
        value = value.trim();
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    public static String formatParameter(String parameter, boolean isGarbled) {
        if (parameter == null)
            return "NULL";
        return (isGarbled ? "N" : "") + "'" + parameter.replace("'", "''") + "'";
    }

    /**
     * 获取原本的参数
     * @param parameter 参数
     * @return 原本的参数
     */
    public static String getOriginalParameter(String parameter) {
        if (isEmptyOrBlank(parameter)) {
            return parameter;
        }
        if (parameter.startsWith("'") && parameter.endsWith("'")) {
            return parameter.substring(1, parameter.length() - 1);
        } else if (parameter.startsWith("N'") && parameter.endsWith("'")) {
            return parameter.substring(2, parameter.length() - 1);
        } else {
            return parameter;
        }

    }


    public static String titleChangeBookName(String title) {
        return title.toLowerCase().split(" by ")[0].split("read aloud|audio book")[0].split("\\|")[0].split("-")[0];
    }

    /**
     * 截断字符串，多余部分使用省略号代替
     *
     * @param value
     * @param length
     * @return
     */
    public static String cutString(String value, int length) {
        if (StringUtil.isEmptyOrBlank(value)) {
            return value;
        }
        value = value.trim();
        if (value.length() <= length) {
            return value;
        }
        return value.substring(0, length - 3) + "...";
    }

    public static final char SBC_SPACE = 12288; // 全角空格 12288

    public static final char DBC_SPACE = 32; //半角空格 32

    // ASCII character 33-126 <-> unicode 65281-65374
    public static final char ASCII_START = 33;

    public static final char ASCII_END = 126;

    public static final char UNICODE_START = 65281;

    public static final char UNICODE_END = 65374;

    public static final char DBC_SBC_STEP = 65248; // 全角半角转换间隔

    public static char sbc2dbc(char src) {
        if (src == SBC_SPACE) {
            return DBC_SPACE;
        }

        if (src >= UNICODE_START && src <= UNICODE_END) {
            return (char) (src - DBC_SBC_STEP);
        }

        return src;
    }

    /**
     * 全角转半角
     *
     * @param src
     * @return DBC case
     */
    public static String sbc2dbcCase(String src) {
        if (src == null) {
            return null;
        }
        char[] c = src.toCharArray();
        for (int i = 0; i < c.length; i++) {
            c[i] = sbc2dbc(c[i]);
        }
        return new String(c);
    }

    public static char dbc2sbc(char src) {
        if (src == DBC_SPACE) {
            return SBC_SPACE;
        }
        if (src <= ASCII_END) {
            return (char) (src + DBC_SBC_STEP);
        }
        return src;
    }

    /**
     * 半角转全角
     *
     * @param src
     * @return SBC case string
     */
    public static String dbc2sbcCase(String src) {
        if (src == null) {
            return null;
        }

        char[] c = src.toCharArray();
        for (int i = 0; i < c.length; i++) {
            c[i] = dbc2sbc(c[i]);
        }

        return new String(c);
    }

    public static String encryptEmail(String email) {
        boolean flag = false;
        int index = email.lastIndexOf(".");
        char[] chars = email.toCharArray();
        int i = 0;
        for (char c : chars) {
            if (index > i) {
                if (Character.isLetterOrDigit(c)) {
                    if (flag) {
                        chars[i] = '*';
                    }
                    flag = true;
                } else {
                    flag = false;
                }
            }
            i++;
        }
        return String.valueOf(chars);
    }

    // 兼容匹配
    public static String getCompatibleResults(String content) {
        if (content == null) {
            return null;
        }
        content = content.trim();
        content = content.replace("\t", "");
        content = StringUtil.sbc2dbcCase(content).replaceAll("\\s+", " ");
        content = content.replaceAll("\\s?[-|_|–]\\s?", " ");
        return content;
    }

    // 疑似匹配
    public static List<String> getSuspectedResults(String content, List<String> results) {
        if (isEmptyOrBlank(content)) {
            return new ArrayList<>();
        }
        List<String> highFrequencyWords = getHighFrequencyWords(results);
        Map<Integer, List<String>> sortMap = new HashMap<>();
        for (String result : results) {
            int level = getSimilarity(content, result, highFrequencyWords);
            if (sortMap.containsKey(level)) {
                sortMap.get(level).add(result);
            } else {
                List<String> list = new LinkedList();
                list.add(result);
                sortMap.put(level, list);
            }
        }
        OptionalInt max = sortMap.keySet().stream().mapToInt(x -> x).max();
        if (max.isPresent() && max.getAsInt() > (maxLevel * 66 / 100)) {
            return sortMap.get(max.getAsInt());
        }
        return new ArrayList<>();
    }

    private static List<String> getHighFrequencyWords(List<String> results) {
        List<String> allWords = new ArrayList<>();
        List<String> highFrequencyWords = new ArrayList<>();
        if (results != null && results.size() > 5) {
            for (String result : results) {
                String[] split = result.split("\\s+");
                allWords.addAll(Arrays.asList(split));
            }
            Map<String, List<String>> map = allWords.stream().map(x -> getCompatibleResults(x)).collect(Collectors.groupingBy(x -> x.toLowerCase()));
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if (entry.getValue().size() * 2 > results.size()) {
                    highFrequencyWords.add(entry.getKey());
                }
            }
        }
        return highFrequencyWords;
    }

    private static final int maxLevel = 1000;

    private static int getSimilarity(String source, String target, List<String> highFrequencyWords) {
        if (source == null && target == null) {
            return maxLevel;
        }
        if (source == null || target == null) {
            return 0;
        }
        // 提取相同的单词
        List<String> newHighFrequencyWords = new LinkedList<>();
        for (String highFrequencyWord : highFrequencyWords) {
            String sourceCompatible = getCompatibleResults(source).toLowerCase();
            String targetCompatible = getCompatibleResults(target).toLowerCase();
            if (sourceCompatible.contains(highFrequencyWord) && targetCompatible.contains(highFrequencyWord)) {
                newHighFrequencyWords.add(highFrequencyWord);
            }
        }
        source = transform(source, newHighFrequencyWords);
        target = transform(target, newHighFrequencyWords);
        int targetLength = target.length();
        int sourceLength = source.length();
        int editDistance = editDis(source, target);
        int num = (int) (((float) editDistance / Math.max(sourceLength, targetLength)) * maxLevel);
        return maxLevel - num;
    }

    private static String transform(String source, List<String> highFrequencyWords) {
        String target = getCompatibleResults(source).toLowerCase();
        int index = 0;
        // 降低高频词在算法中的占比
        for (String highFrequencyWord : highFrequencyWords) {
            target = target.replace(highFrequencyWord, "@" + index++);
        }
        return target.trim();
    }

    private static int editDis(String a, String b) {

        int aLen = a.length();
        int bLen = b.length();

        if (aLen == 0) return aLen;
        if (bLen == 0) return bLen;

        int[][] v = new int[aLen + 1][bLen + 1];
        for (int i = 0; i <= aLen; ++i) {
            for (int j = 0; j <= bLen; ++j) {
                if (i == 0) {
                    v[i][j] = j;
                } else if (j == 0) {
                    v[i][j] = i;
                } else if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    v[i][j] = v[i - 1][j - 1];
                } else {
                    v[i][j] = 1 + Math.min(v[i - 1][j - 1], Math.min(v[i][j - 1], v[i - 1][j]));
                }
            }
        }
        return v[aLen][bLen];
    }

    public static String removeSpaceAndFullPitch(String str) {
        if (str == null) {
            return null;
        }
        str = str.replace(" ", "").replace("\t", "");
        str = StringUtil.sbc2dbcCase(str);
        return str;
    }

    public static String getRepeatName(List<String> names, String name, int i) {
        String repeatName = name;
        if (i > 0) {
            repeatName = name + "(" + i + ")";
        }
        if (names.contains(repeatName)) {
            return getRepeatName(names, name, i + 1);
        } else {
            return repeatName;
        }
    }

    private static final Map<String, Integer> MONTH_MAP = new HashMap() {{
        put("january", 1);
        put("jan", 1);
        put("february", 2);
        put("feb", 2);
        put("march", 3);
        put("mar", 3);
        put("april", 4);
        put("apr", 4);
        put("may", 5);
        put("june", 6);
        put("jun", 6);
        put("july", 7);
        put("jul", 7);
        put("august", 8);
        put("aug", 8);
        put("september", 9);
        put("sept", 9);
        put("october", 10);
        put("oct", 10);
        put("november", 11);
        put("nov", 11);
        put("december", 12);
        put("dec", 12);
    }};

    public static String conversionMonth(String name) {
        String[] words = name.split("\\b");
        for (String word : words) {
            if (MONTH_MAP.containsKey(word.toLowerCase())) {
                Integer num = MONTH_MAP.get(word.toLowerCase());
                name = name.replaceAll("\\b" + word + "\\b", num.toString());
            }
        }
        return name;
    }

    public static String firstText(String... texts) {
        for (String text : texts) {
            if (!isEmptyOrBlank(text)) {
                return text;
            }
        }
        return null;
    }

    /**
     * 采用逗号分隔符，分隔字符串为数组，并去掉两侧空字符串和过滤掉空字符串
     *
     * @param commaSeparatedStr 目标字符串
     * @return 分隔后的数组
     */
    public static String[] split(String commaSeparatedStr) {
        if (commaSeparatedStr == null) {
            return new String[0];
        }
        return Stream.of(commaSeparatedStr.split(StringUtil.COMMA))
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .toArray(String[]::new);
    }

    /**
     * 采用空格分隔符，分隔字符串为数组，并去掉两侧空字符串和过滤掉空字符串
     *
     * @param commaSeparatedStr 目标字符串
     * @return 分隔后的数组
     */
    public static String[] splitBySpace(String commaSeparatedStr) {
        if (commaSeparatedStr == null) {
            return null;
        }
        return Stream.of(commaSeparatedStr.split(StringUtil.SPACE))
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .toArray(String[]::new);
    }

    /**
     * 移除 ID 中的后缀
     * @param id 原 ID
     * @return 处理后的 ID
     */
    public static String removeIdSuffix(String id) {
        if (StringUtil.isEmptyOrBlank(id)) {
            return id;
        }
        id = id.trim();
        // 结尾不包含 LG，不用处理
        if (id.length() != 38 && !id.toUpperCase().endsWith("LG")) {
            return id;
        }
        // 替换到结尾
        return id.substring(0, id.length() - 2);
    }

    /**
     * 安全的去除字符串的前后空格
     * @param str 原字符串
     * @return 去除前后空格的字符串
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

	/**
     * 格式化数字为字符串，显示正负号与百分号，若为零，返回 --
     * @param value 需要格式化的数字
     * @return 格式化后的字符串
     */
    public static String formatComparedRate(int value) {
        if (value == 0) {
            return "--";
        } else if (value > 0) {
            return String.format("↑ %d%%", value);
        } else {
            return String.format("↓ %d%%", Math.abs(value));
        }
    }

    /**
     * 格式化数字为字符串，显示正负号，若为零，返回 0
     * @param value 需要格式化的数字
     * @return 格式化后的字符串
     */
    public static String formatComparedRate0(int value) {
        if (value == 0) {
            return "0";
        } else if (value > 0) {
            return String.format("↑ %d", value);
        } else {
            return String.format("↓ %d", Math.abs(value));
        }
    }

    /**
     * 格式化数字为字符串，显示正负号，若为零，返回 --
     * @param value 需要格式化的数字
     * @return 格式化后的字符串
     */
    public static String formatCompared(int value) {
        if (value == 0) {
            return "--";
        } else {
            return String.format("%+d", value);
        }
    }

    /**
     * 字符串内容是否为数字
     * @param string 字符串
     * @return 是否为数字
     */
    public static boolean isNumber(String string) {
        if (string == null) {
            return false;
        }
        return string.trim().matches("[0-9]+");
    }

    /**
     * 获取指定正则匹配到的结果集合
     * @param content 要匹配的内容
     * @param regex  正则表达式
     * @return 匹配到的结果集合
     */
    public static List<String> getMatchValuesByRegex(String content, String regex) {
        // 结果
        List<String> result = new ArrayList<>();
        // 正则
        Pattern pattern = Pattern.compile(regex);
        // 匹配
        Matcher matcher = pattern.matcher(content);
        // 遍历所有匹配到的结果
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    /**
     * 获取指定正则匹配的第一个分组内容
     * @param content 要匹配的内容
     * @param regex  正则表达式
     * @return 匹配到的第一个分组内容
     */
    public static String getFirstMatchGroupValueByRegex(String content, String regex) {
        // 正则
        Pattern pattern = Pattern.compile(regex);
        // 匹配
        Matcher matcher = pattern.matcher(content);
        // 是否匹配到结果
        if (matcher.find() && matcher.groupCount() > 0) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 按照 regex 规则，将字符串转换为驼峰命名法
     * @param s 字符串
     * @param regex 分割
     * @return 转驼峰后的字符串
     */
    public static String camelCase(String s, String regex) {
        if (StringUtil.isEmptyOrBlank(s) || StringUtil.isEmptyOrBlank(regex)) {
            return s;
        }
        String[] words = s.split(regex);
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                sb.append(word.substring(0, 1).toUpperCase());
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 字符串内容是否为 NULL 或 null
     * @param string 字符串
     * @return 格式化后的字符串
     */
    public static String formatContentNull(String string) {
        if (string == null) {
            return null;
        }
        if (string.trim().equalsIgnoreCase("null")) {
            return "";
        } else {
            return string;
        }
    }

    /**
     * 对被字符分割的字符串过滤空值并去重
     *
     * @param inputString 被字符串分隔符分隔字符串
     * @param splitter     字符串分隔符
     * @return 过滤空值并去重后的字符串
     */
    public static String removeEmptyAndDuplicates(String inputString, String splitter) {
        // 如果 inputString 为空，那么直接返回空字符串
        if (StringUtil.isEmptyOrBlank(inputString)) {
            return inputString;
        }
        // 使用分隔符分割字符串，转换为流，进行 trim 处理，过滤空值，去重，然后使用分隔符拼接
        return Arrays.stream(inputString.split(splitter))
                .map(String::trim)
                .filter(s -> !StringUtil.isEmptyOrBlank(s))
                .distinct()
                .collect(Collectors.joining(splitter));
    }

    /**
     * 获取标准字符的名称
     * @param name 名称
     * @return
     */
    public static String getStandardName(String name) {
        if (StringUtil.isEmptyOrBlank(name)) {
            return name;
        }
        // 定义要保留的常见字符集的正则表达式
        String commonCharactersRegex = "[\\p{L}\\p{N}\\s]"; // 包含字母、数字和空格

        // 使用正则表达式匹配常见字符集
        Pattern pattern = Pattern.compile(commonCharactersRegex);
        Matcher matcher = pattern.matcher(name);

        // 过滤掉非常见字符集
        StringBuilder resultBuilder = new StringBuilder();
        while (matcher.find()) {
            resultBuilder.append(matcher.group());
        }
        // 将空白字符替换为一个空格
        resultBuilder = new StringBuilder(resultBuilder.toString().replaceAll("\\s+", " "));

        // 输出过滤后的字符串
        return resultBuilder.toString().trim();
    }

    /**
     * 拼接字符串
     * @param args 字符串
     * @return 拼接后的结果
     */
    public static String joinString(String ...args) {
        StringBuilder sb = new StringBuilder();
        for (String str : args) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 判断字符串是否为包含 HTML 标签
     *
     * @param text 文本内容
     * @return 是否包含 HTML 标签
     */
    public static boolean isHTML(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        Matcher matcher = HTML_PATTERN.matcher(text);
        return matcher.find();
    }
}
