package com.example.common_ground.account.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Retention: 애노테이션이 런타임 동안 유지된다
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER) // 메서드 파라미터에 적용될 수 있음
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
public @interface CurrentUser {
}
