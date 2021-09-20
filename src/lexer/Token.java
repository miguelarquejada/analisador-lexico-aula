package lexer;

public class Token {

    private String lexema;
    private Type type;

    public Token(String lexema, Type type) {
        this.lexema = lexema;
        this.type = type;
    }

    public String getLexema() {
        return lexema;
    }

    public Type getType() {
        return type;
    }
}
