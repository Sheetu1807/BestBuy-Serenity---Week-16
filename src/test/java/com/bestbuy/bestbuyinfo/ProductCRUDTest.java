package com.bestbuy.bestbuyinfo;

import com.bestbuy.constants.EndPoints;
import com.bestbuy.model.ProductPojo;
import com.bestbuy.testbase.TestBase;
import com.bestbuy.utils.TestUtils;
import io.restassured.http.ContentType;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.HashMap;
import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class ProductCRUDTest extends TestBase {

    static String name = "Apple" + TestUtils.getRandomValue();
    static String type = "XPro" + TestUtils.getRandomValue();
    static String model = "iPhone 14" + TestUtils.getRandomValue();
    static Object productsID;
    @Test // get all list
    public void test001() {

        SerenityRest.given()
                .when()
                .log().all()
                .get(EndPoints.GET_ALL_PRODUCTS)
                .then().log().all().statusCode(200);
    }

    @Test // post new and retrieve id
    public void test002() {

        ProductPojo pojo = new ProductPojo();
        pojo.setName(name);
        pojo.setType(type);
        pojo.setPrice(1700);
        pojo.setShipping(15);
        pojo.setUpc("012345678910");
        pojo.setDescription("Switch from Android to iPhone");
        pojo.setManufacturer("Pegatron");
        pojo.setModel("iPhone 14 Pro");
        pojo.setUrl("https://www.apple.com/uk/iphone-14-pro/");
        pojo.setImage("https://www.apple.com/euro/iphone-14-pro/a/screens_alt/images/overview/design/design_startframe__cffzwjeyro2q_large.jpg");


        SerenityRest.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(pojo)
                .when()
                .post(EndPoints.CREATE_NEW_PRODUCTS)
                .then().log().all()
                .statusCode(201);
        System.out.println(name);
    }
    @Title("Verify if product was created")
    @Test
    public void test003() {

        String p1 = "data.findAll{it.name='";
        String p2 = "'}.get(0)";
        HashMap<String,?> productMap = SerenityRest.given().log().all()
                .when()
                .get(EndPoints.GET_ALL_PRODUCTS)
                .then().statusCode(200)
                .extract()
                .path(p1 + name + p2);
        System.out.println();
        String name1 = "iPhone 14";
        //Assert.assertThat(productMap,hasValue(name));
        productsID = productMap.get("id");
        System.out.println("ID =" + productsID);
        System.out.println("name = " + productMap);
    }

    @Title("update the product and verify the update information")
    @Test
    public void test004() {
        name = name +"abc";
        type = type + "patel";

        ProductPojo pojo = new ProductPojo();
        pojo.setName(name);
        pojo.setType(type);


        SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("productsID", productsID)
                .body(pojo)
                .when()
                .patch(EndPoints.UPDATE_PRODUCT_BY_ID)
                .then().log().all().statusCode(200);


        String p1 = "data.findAll{it.name='";
        String p2 = "'}.get(0)";
        HashMap<String, ?> productData  = SerenityRest.given().log().all()
                .when()
                .get(EndPoints.GET_ALL_PRODUCTS)
                .then().statusCode(200)
                .extract()
                .path(p1 + name + p2);
        System.out.println(productData);
        Assert.assertThat(productData, hasValue(name));
    }

    @Title("Delete the student and verify if the student is deleted")
    @Test
    public void test005(){

        SerenityRest.given()
                .pathParam("productsID",productsID)
                .when()
                .delete(EndPoints.DELETE_PRODUCT_BY_ID)
                .then().log().all().statusCode(200);

        SerenityRest.given()
                .pathParam("productsID",productsID)
                .when()
                .get(EndPoints.DELETE_PRODUCT_BY_ID)
                .then().log().all().statusCode(404);

    }

}




