package com.kromanenko.appservice;

import org.springframework.boot.SpringApplication;

public class TestAppServiceApplication
{

  public static void main(String[] args)
  {
    SpringApplication.from(AppServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
  }

}
