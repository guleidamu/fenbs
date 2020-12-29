package com.damu.febs.common.annotation;

import com.damu.febs.common.configure.FebsServerProtectConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FebsServerProtectConfigure.class)
public @interface EnableFebsServerProtect {

}
