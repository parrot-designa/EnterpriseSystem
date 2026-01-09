package com.enterprisesystem.babygateway.config;

import java.util.ArrayList;
import java.util.List;

public interface AclProvider {

    default List<String> blackList() { return new ArrayList<>();}

    default List<String> whiteList() { return new ArrayList<>();}

    default List<String> unConcurrencyControlList(){return new ArrayList<>();}

}
