package cc.catman.object.example.template;

import cc.catman.object.ObjectPath;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 不能直接使用,因为其没有在替换为文本后,重新计算后续的表达式位置
 */
public class ObjectPathTemplate {

    private String regex="\\$\\{(.*?)\\}";

    public String format(String template,Object context){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(template);
        // 依次替换所有匹配的表达式
        while (matcher.find()) {
            MatchResult matchResult = matcher.toMatchResult();
            String expr = matchResult.group(1);
            // 解析表达式并替换
            String value = ObjectPath.of(expr).eval(context, String.class);
            // 但是需要注意的是,每次拼接后,需要将匹配到的内容替换掉,否则会出现重复替换的情况
            template = template.substring(0, matchResult.start()) + value + template.substring(matchResult.end());
        }
        return template;
    }
}