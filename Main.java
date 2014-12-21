import java.lang.reflect.Field;
import java.text.StringCharacterIterator;
import java.util.Random;

/**
 * Created by raghavnarula on 20/12/2014.
 */
public class Main {
    public static final char CHAR_TO_STRIP = 'a';
    public static int MIN_LENGTH = 10;
    public static int MAX_LENGTH = 100000;

    public static String buildString(final int length){
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; ++i) {
            char c = chars[random.nextInt(chars.length)];
            builder.append(c);
        }
        return builder.toString();
    }

    public static int countChar(final char c, final String s){
        int count = 0;
        for(int i = 0; i < s.length(); ++i ){
            if(c == s.charAt(i)){
                ++count;
            }
        }
        return count;
    }

    private static int getNext(int type, int i){
        switch(type){
            case 0:
                i=((i/100+1)); //log
                break;
            case 1:
                i=100; //linear
                break;
        }
        return i;
    }

    public static void main(String... args) throws NoSuchFieldException, IllegalAccessException {

        if(args.length != 4){
            System.out.println("Not enough parameters, exiting. Correct useage: " +
                    "<algorithm selector (0 = recursive)> <step type (0 = log, 1 = linear)> " +
                    "<min length> <max length>");
            System.exit(-1);
        }

        MIN_LENGTH = Integer.parseInt(args[2]);
        MAX_LENGTH = Integer.parseInt(args[3]);

        Runtime runtime = Runtime.getRuntime();
        long totalmem = runtime.totalMemory();

        for (int i = MIN_LENGTH; i < MAX_LENGTH; i+=getNext(Integer.parseInt(args[1]), i)) {
            String testString = buildString(i);
            double ratio = (countChar(CHAR_TO_STRIP, testString) /(double) i);
            long startTime = 0;
            long stopTime = 0;
            long membefore = 0;
            long memafter = 0;
            switch(Integer.parseInt(args[0])){
                case 0:
                    membefore = runtime.maxMemory() - runtime.freeMemory();
                    startTime = System.nanoTime();
                    stripChars_recursive(testString, CHAR_TO_STRIP);
                    stopTime = System.nanoTime();
                    memafter = runtime.maxMemory() - runtime.freeMemory();
                    break;
                case 1:
                    membefore = runtime.totalMemory() - runtime.freeMemory();
                    startTime = System.nanoTime();
                    stripChars_rec2(testString, CHAR_TO_STRIP);
                    stopTime = System.nanoTime();
                    memafter = runtime.totalMemory() - runtime.freeMemory();
                    break;
                case 2:
                    membefore = runtime.totalMemory() - runtime.freeMemory();
                    startTime = System.nanoTime();
                    stripChars_iter(testString, CHAR_TO_STRIP);
                    stopTime = System.nanoTime();
                    memafter = runtime.totalMemory() - runtime.freeMemory();
                    break;
                case 3:
                    membefore = runtime.totalMemory() - runtime.freeMemory();
                    startTime = System.nanoTime();
                    stripChars_iter2(testString, CHAR_TO_STRIP);
                    stopTime = System.nanoTime();
                    memafter = runtime.totalMemory() - runtime.freeMemory();
                    break;
            }
            System.out.printf("%d %f %d %d\n", i, ratio, (stopTime - startTime), memafter - membefore);
        }
    }


    public static String stripChars_recursive(final String s, char c){
        if(s.length() == 0) {
            return s;
        }

        if(s.charAt(0) == c){
            return stripChars_recursive(s.substring(1), c);
        }

        return s.substring(0,1) + stripChars_recursive(s.substring(1), c);
    }

    public static String stripChars_rec2(final String text, char c){
        StringCharacterIterator iter = new StringCharacterIterator(text);
        StringBuilder builder = new StringBuilder();

        return stripChars_rec2_impl(iter,builder,c);
    }

    public static String stripChars_rec2_impl(StringCharacterIterator iter, StringBuilder builder,final char c){
        if(iter.getIndex() == iter.getEndIndex()){
            return builder.toString();
        }
        if(iter.current() != c){
            builder.append(iter.current());
        }
        iter.setIndex(iter.getIndex() + 1);
        return stripChars_rec2_impl(iter,builder,c);
    }

    public static String stripChars_iter(final String s, final char c){
        StringBuilder builder = new StringBuilder();
        int length = s.length();
        for(int i = 0; i < length; ++i){
            if(c != s.charAt(i)){
                builder.append(s.charAt(i));
            }
        }
        return builder.toString();
    }

    public static String stripChars_iter2(String s, final char c) throws NoSuchFieldException, IllegalAccessException {
        Field stringValueField = String.class.getDeclaredField("value");
        stringValueField.setAccessible(true);

        char[] value = (char[])stringValueField.get(s);

        int count = 0;
        for(int i = 0; i < value.length; ++i){
            if(value[i] == c){
                ++count;
            }
        }
        if(count > 0 ) {
            char[] valueTrimmed = new char[value.length - count];
            count = 0;
            for (int i = 0; i < value.length; ++i) {
                if (value[i] != c) {
                    valueTrimmed[count] = value[i];
                    ++count;
                }
            }
            stringValueField.set(s, valueTrimmed);
        }

        return s;
    }

}
