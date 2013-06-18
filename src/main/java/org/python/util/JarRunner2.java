package org.python.util;

import org.python.core.imp;
import org.python.core.PySystemState;

public class JarRunner2 {

    public static void run(String[] args) {
        final String runner = "__run__";
        String[] argv = new String[args.length + 1];
        argv[0] = runner;
        System.arraycopy(args, 0, argv, 1, args.length);
        PySystemState.initialize(PySystemState.getBaseProperties(), null, argv);
        imp.load(runner);
    }

    public static void main(String[] args) {
        run(args);
    }
}
