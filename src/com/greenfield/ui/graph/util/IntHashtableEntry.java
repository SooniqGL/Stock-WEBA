// Source File Name:   IntHashtable.java

package com.greenfield.ui.graph.util;


class IntHashtableEntry
{

    IntHashtableEntry()
    {
    }

    protected Object clone()
    {
        IntHashtableEntry inthashtableentry = new IntHashtableEntry();
        inthashtableentry.hash = hash;
        inthashtableentry.key = key;
        inthashtableentry.value = value;
        inthashtableentry.next = next == null ? null : (IntHashtableEntry)next.clone();
        return inthashtableentry;
    }

    int hash;
    int key;
    Object value;
    IntHashtableEntry next;
}
