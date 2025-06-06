public class WordleResponse {
    private char c;
    private int index;
    private LetterResponse resp;

    public WordleResponse(char c, int index, LetterResponse resp) {
        this.c = c;
        this.index = index;
        this.resp = resp;
    }
    public char getChar() {
        return c;
    }
    public int getIndex() {
        return index;
    }
    public LetterResponse getResp() {
        return resp;
    }
}
