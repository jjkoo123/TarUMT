public class p2q4 {
    public static void main(String[] args){
        String text = "abcdef" ;
        System.out.println("String before reverse: " + text);

        StringBuffer stringbuffer = new StringBuffer(text);

        stringbuffer.reverse();
        System.out.println("String after reverse: " + stringbuffer);
    }
}
