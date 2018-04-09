package es.ffgiraldez.comicsearch.rx;

import es.ffgiraldez.comicsearch.ReplaceToVoid;
import rx.functions.Func1;

public class Replacements {
    public static Func1<Object, Void> returnVoid() {
        return new ReplaceToVoid();
    }
}
