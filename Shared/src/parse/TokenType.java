package parse;

import static parse.TokenCategory.ACTION;
import static parse.TokenCategory.ADDOP;
import static parse.TokenCategory.MEMSUGAR;
import static parse.TokenCategory.MULOP;
import static parse.TokenCategory.OTHER;
import static parse.TokenCategory.RELOP;
import static parse.TokenCategory.SENSOR;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

enum TokenType {
    MEM(OTHER, "mem"),
    WAIT(ACTION, "wait"),
    FORWARD(ACTION, "forward"),
    BACKWARD(ACTION, "backward"),
    LEFT(ACTION, "left"),
    RIGHT(ACTION, "right"),
    EAT(ACTION, "eat"),
    ATTACK(ACTION, "attack"),
    GROW(ACTION, "grow"),
    BUD(ACTION, "bud"),
    MATE(ACTION, "mate"),
    TAG(ACTION, "tag"),
    SERVE(ACTION, "serve"),
    OR(OTHER, "or"),
    AND(OTHER, "and"),
    LT(RELOP, "<"),
    LE(RELOP, "<="),
    EQ(RELOP, "="),
    GE(RELOP, ">="),
    GT(RELOP, ">"),
    NE(RELOP, "!="),
    PLUS(ADDOP, "+"),
    MINUS(ADDOP, "-"),
    MUL(MULOP, "*"),
    DIV(MULOP, "/"),
    MOD(MULOP, "mod"),
    ASSIGN(OTHER, ":="),
    NEARBY(SENSOR, "nearby"),
    AHEAD(SENSOR, "ahead"),
    RANDOM(SENSOR, "random"),
    SMELL(SENSOR, "smell"),
    LBRACKET(OTHER, "["),
    RBRACKET(OTHER, "]"),
    LPAREN(OTHER, "("),
    RPAREN(OTHER, ")"),
    LBRACE(OTHER, "{"),
    RBRACE(OTHER, "}"),
    ARR(OTHER, "-->"),
    SEMICOLON(OTHER, ";"),
    ABV_MEMSIZE(MEMSUGAR, "MEMSIZE"),
    ABV_DEFENSE(MEMSUGAR, "DEFENSE"),
    ABV_OFFENSE(MEMSUGAR, "OFFENSE"),
    ABV_SIZE(MEMSUGAR, "SIZE"),
    ABV_ENERGY(MEMSUGAR, "ENERGY"),
    ABV_PASS(MEMSUGAR, "PASS"),
    ABV_TAG(MEMSUGAR, "TAG"),
    ABV_POSTURE(MEMSUGAR, "POSTURE"),
    NUM(OTHER, "<number>"),
    ERROR(OTHER, "[error]"),
    EOF(OTHER, "EOF");

    private static final Map<String, TokenType> stringToTypeMap;
    static {
        final Map<String, TokenType> temp = new HashMap<>();
        for (final TokenType t : values()) {
            temp.put(t.stringRep, t);
        }
        stringToTypeMap = Collections.unmodifiableMap(temp);
    }

    final TokenCategory category;
    final String stringRep;

    private TokenType(final TokenCategory category, final String stringRep) {
        this.category = category;
        this.stringRep = stringRep;
    }

    public static TokenType getTypeFromString(final String rep) {
        return stringToTypeMap.get(rep);
    }
}
