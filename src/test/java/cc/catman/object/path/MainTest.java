package cc.catman.object.path;

import cc.catman.object.GsonCoder;
import cc.catman.object.Mock;
import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.DefaultObjectPathParserContext;
import cc.catman.object.core.ObjectPathVisitor;
import cc.catman.object.core.accessor.*;
import cc.catman.object.core.accessor.matcher.DefaultClassNameMatcher;
import cc.catman.object.core.function.DefaultFunctionManager;
import cc.catman.object.core.rewrite.AggregationObjectRewrite;
import cc.catman.object.core.script.DefaultScriptExecutorManager;
import cc.catman.object.path.standard.ObjectPathLexer;
import cc.catman.object.path.standard.ObjectPathParser;
import cc.catman.object.cases.circle.Library;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void simple() {
        Library library = Mock.mockLibrary(20,10);
        // 创建visitor
        ObjectPathVisitor visitor = new ObjectPathVisitor(createContext(library));
        String first="$.name";
        Object res = createParser(first).objectPath().accept(visitor);
        assertEquals(library.getName(),res);
    }

    @Test
    public void wildcard() {
        Library library = Mock.mockLibrary(20,10);
        // 创建visitor
        ObjectPathVisitor visitor = new ObjectPathVisitor(createContext(library));
        String first="$.books[*].authors|[^]|[0].name";
        Object res = createParser(first).objectPath().accept(visitor);
        assertEquals(library.getBooks().get(0).getAuthors().get(0).getName(),res);
    }

    @Test
    public void forPipe() {
        Library library = Mock.mockLibrary(20,10);
        // 创建visitor
        ObjectPathVisitor visitor = new ObjectPathVisitor(createContext(library));
        String first="$.books[*].authors|[^]|[1:3]";
        Object res = createParser(first).objectPath().accept(visitor);
        assertEquals(library.getBooks().get(0).getAuthors().subList(1,3),res);
    }

    @Test
    public void filter(){
        List<Map<String,Integer>> list=new ArrayList<>();
        Map<String,Integer> map=new HashMap<>();
        map.put("a",1);
        list.add(map);
        Map<String,Integer> map2=new HashMap<>();
        map2.put("a",2);
        list.add(map2);
        Map<String,Integer> map3=new HashMap<>();
        map3.put("a",3);
        list.add(map3);
        Map<String,Integer> map4=new HashMap<>();
        map4.put("a",4);
        list.add(map4);
        // 创建visitor
        ObjectPathVisitor visitor = new ObjectPathVisitor(createContext(list));
        String first="$[0].a";
        Object res = createParser(first).objectPath().accept(visitor);
        assertEquals(1,res);
        String second="$[?(a==1)]";
        Object res2 = createParser(second).objectPath().accept(visitor);
        assertEquals(list.get(0),((List<?>)res2).get(0));
    }
    
    public DefaultObjectPathParserContext createContext(Object root){
        AggregateObjectAccessor aoa = new AggregateObjectAccessor()
                .add(new BasicObjectAccessor(Collections.singletonList(new DefaultClassNameMatcher()
                        .addPackage(Integer.class.getPackage())
                        .addClasses(int.class, Integer.class, Double.class, Float.class, Long.class, Short.class, Byte.class, Character.class, Boolean.class, String.class)
                )))
                .add(new ArrayObjectAccessor())
                .add(new MapObjectAccessor())
                .add(new DefaultClassObjectAccessor());

        GsonCoder gsonCoder = new GsonCoder();
        ObjectPathConfiguration config = ObjectPathConfiguration.builder()
                .objectAccessor(aoa)
                .jsonCoder(gsonCoder)
                .objectRewrite(new AggregationObjectRewrite())
                .functionManager(new DefaultFunctionManager())
                .scriptExecutorManager(new DefaultScriptExecutorManager())
                .build();

        return new DefaultObjectPathParserContext(root,config);
    }


    public ObjectPathParser createParser(String path){
        CodePointCharStream stream = CharStreams.fromString(path);
        ObjectPathLexer lexer = new ObjectPathLexer(stream);
        return new ObjectPathParser(new CommonTokenStream(lexer));
    }
}