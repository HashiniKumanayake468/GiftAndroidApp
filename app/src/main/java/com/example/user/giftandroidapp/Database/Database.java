package com.example.user.giftandroidapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.user.giftandroidapp.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 7/27/2018.
 */

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME="GiftAppDB.db";
    private static final int DB_VER=1;

    public Database(Context context) {
        super(context, DB_NAME,null, DB_VER);
    }

    public List<Order> getCarts()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect={"ProductName","ProductId","Quantity","Price","Discount"};
        String sqlTable="OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst())
        {
            do {
                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount"))



                ));
            }while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query  = String.format("INSERT INTO OrderDetail(ProductId,ProductName,Quantity,Price,Discount)VALUES('%s','%s','%s','%s','%s');",
                    order.getProductId(),
                    order.getProductName(),
                    order.getQuantity(),
                    order.getPrice(),
                    order.getDiscount());
        db.execSQL(query);
    }


    public void cleanCart()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query  = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }


    //favourites
    public void addToFavorites(String giftId)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(GiftId) VALUES('%s');",giftId);
        db.execSQL(query);
    }

    public void removeFromFavorites(String giftId)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE GiftId='%s';",giftId);
        db.execSQL(query);
    }

    public boolean isFavorite(String giftId)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE GiftId='%s';",giftId);
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.getCount() <= 0)
        {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}