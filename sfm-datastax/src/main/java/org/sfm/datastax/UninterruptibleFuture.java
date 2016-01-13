package org.sfm.datastax;

import com.google.common.util.concurrent.ListenableFuture;


public interface UninterruptibleFuture<T> extends ListenableFuture<T> {
    T getUninterruptibly();
}
