package org.yearup.data.mysql;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.junit.jupiter.MockitoExtension;
import org.yearup.controllers.CategoriesController;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoriesDaoTest {
    /*
     *Mockito creates a mock(clone) for your class and gives developer
     * more flexibility to test.
     */
    @Mock CategoryDao categoryDao;
    @Mock ProductDao productDao;
    @Mock DataSource dataSource;
    @Mock Connection connection;
    @Mock PreparedStatement ps;
    @Mock ResultSet generatedKeys;

    @InjectMocks
    CategoriesController controller;
    @InjectMocks
    MySqlCategoryDao mySqlCategoryDao;

    @Test
    public void getAllCategoriesEmptyListTest(){
        //arrange
        when(categoryDao.getAllCategories()).thenReturn(List.of());
        //act
        List<Category> categoryList = categoryDao.getAllCategories();
        //assert
        assertThat(categoryList).isEmpty();
        verify(categoryDao).getAllCategories();

    }

    @Test
    public void getAllCategoriesTest(){
        List<Category> expectedCategoryList = new ArrayList<>();
        expectedCategoryList.add(new Category(1,"charger","A phone charger"));
        expectedCategoryList.add(new Category(2, "iphone", "An iphone"));
        //arrange
        when(categoryDao.getAllCategories()).thenReturn(expectedCategoryList);
        //act
        List<Category> categoryList = controller.getAll();
        //assert
        assertThat(categoryList).isNotEmpty()
                .isEqualTo(expectedCategoryList)
                .hasSize(2);

    }
    @Test
    public void getCategoriesByIdTest(){
        Category charger = new Category(1, "charger", "A phone charger");
        Category iphone = new Category(2, "iphone", "An iPhone");
        //arrange
        when(categoryDao.getById(1)).thenReturn(charger);
        //act
        Category category = controller.getById(1);
        //assert
        assertThat(category).isEqualTo(charger);
        verify(categoryDao).getById(1);

    }
    @Test
    public void getCategoryByIdReturnNullTest() {
        // Arrange
        when(categoryDao.getById(99)).thenReturn(null);

        // Act
        Category result = controller.getById(99);

        // Assert
        assertThat(result).isNull();
        verify(categoryDao).getById(99);
    }

    @Test
    public void createCategoryUsingGeneratedKeysTest() throws SQLException {
        Category category = new Category(0, "Seafood", "sea food and the goods");
        Category expectedCategory = new Category(101, "Seafood", "sea food and the goods");

        when(categoryDao.create(category)).thenReturn(expectedCategory);

        // Act
        Category result = categoryDao.create(category);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCategoryId()).isEqualTo(101);
        verify(categoryDao).create(category);
    }

    @Test
    public void updateCategoryTest() throws SQLException {
        // Arrange
        int categoryId = 6;
        Category updatedCategory = new Category(categoryId, "Toys", "Baby toys");

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(6);

        //Act
        boolean updateResult = mySqlCategoryDao.update(categoryId,updatedCategory);

        //Assert
        assertThat(updateResult).isTrue();
    }

    @Test
    public void deleteCategoryTest() throws SQLException {
        // Arrange
        int categoryId = 7;

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1);   // simulate 1 row affected

        // Act
        boolean result = mySqlCategoryDao.delete(categoryId);

        // Assert
        assertThat(result).isTrue();
    }

}
