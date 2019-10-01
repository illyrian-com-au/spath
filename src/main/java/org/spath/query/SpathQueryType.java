package org.spath.query;

public enum SpathQueryType {
    ROOT,     // Begins or continues an absolute spath, e.g. "/fred" or "/fred/barnie"
    RELATIVE, // Begins a relative spath, e.g. "//bert" or "/fred//bert"
    ELEMENT,  // Continues a relative spath, e.g. /fred//bert/ernie
    TERMINAL, // Ends an absolute or relative path e.g. "/fred/text()" or "//bert/text()"
}