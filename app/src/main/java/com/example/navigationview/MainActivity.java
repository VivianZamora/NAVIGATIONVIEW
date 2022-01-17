package com.example.navigationview;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.navigationview.ui.gallery.GalleryFragment;
import com.example.navigationview.ui.home.HomeFragment;
import com.example.navigationview.ui.slideshow.SlideshowFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.navigationview.databinding.ActivityMainBinding;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    JSONObject user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            user=new JSONObject(getIntent().getStringExtra("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //obtiene el botón en la interfaz principal
        drawerLayout = findViewById(R.id.drawer_layout);
        //este botón será quien va a abrir el menú de la aplicación

        findViewById(R.id.imageOpenMenu).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //se le asigna la acción de abrir el menú
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                }
        );

        //obtiene el navigation view en donde estará ubicado elementos como
        NavigationView navigationView = findViewById(R.id.id_opctions_menu);
        navigationView.setItemIconTintList(null);

        //establece el frame utilizado para la presentación de las ventanas
        NavController navController = Navigation.findNavController(MainActivity.this, R.id.content_frame);
        //asigna la vista de navegación al controlador
        NavigationUI.setupWithNavController(navigationView, navController);

        //obtenemos el encabezado del menú
        View headMenu = navigationView.getHeaderView(0);
        //modifica el encabezado
        try {
            Encabezado(headMenu);

       Menu bodyMenu = navigationView.getMenu();

        dinamicMenu(bodyMenu);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //loadFragment(new home_fragment());
    }

    private void Encabezado(View headMenu) throws JSONException {
        //se modifica el label, estableciendo el nombre del usuario logeado.

        String userName = (user.getString("name").trim()).toUpperCase();
        userName = userName.length() > 15 ? userName.substring(0, 13) + "..." : userName;

        ((TextView) headMenu.findViewById(R.id.textname)).setText(userName);
        //de igual manera, se modifica la imagen por la foto del usuario
        ImageView userImage = headMenu.findViewById(R.id.profile_image);
        Glide.with(headMenu)
                .load(user.getString("img"))
                .error(R.drawable.user)
                .into(userImage);
    }

    private void dinamicMenu(Menu menu) throws JSONException {
        //obtiene el permiso del usuario, para asi determinar que opciones se van a generar y cuales no
        String rol = user.getString("rol");

        List<MenuItem> menus = new ArrayList<MenuItem>();
        //genera cada uno de los items para el menú
        MenuItem portal = menu.add("Portal");
        portal.setIcon(R.drawable.ic_baseline_home_24);
        portal.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                loadFragment(new HomeFragment());

                return true;
            }
        });

        MenuItem profile = menu.add("Perfil de usuario");
        profile.setIcon(R.drawable.ic_baseline_person_24);
        profile.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                loadFragment(new SlideshowFragment());
                return true;
            }
        });

        MenuItem tmp;
        if (rol.equals("1")) {
            SubMenu navigation_root = menu.addSubMenu("Administración");
            tmp = navigation_root.add("Gestionar Usuarios").setIcon(R.drawable.ic_baseline_supervised_user_circle_24);
            menus.add(tmp);
        }
        if (rol.matches("[12]")) {
            SubMenu navigation_admin = menu.addSubMenu("Gestión");
            tmp = navigation_admin.add("Gestionar Promociones").setIcon(R.drawable.icon_route);
            menus.add(tmp);
            tmp = navigation_admin.add("Gestionar Productos").setIcon(R.drawable.icon_location);
            menus.add(tmp);
        }
        if (rol.matches("[123]")) {
            SubMenu navigation_user = menu.addSubMenu("Opciones Generales");
            tmp = navigation_user.add("Visualizar Promociones").setIcon(R.drawable.icon_route);
            menus.add(tmp);
            tmp = navigation_user.add("Ver Productos").setIcon(R.drawable.icon_location);
            menus.add(tmp);
        }

        SubMenu navigation_others = menu.addSubMenu("Otros");
        tmp = navigation_others.add("Configuración");
        tmp.setIcon(R.drawable.ic_baseline_settings_24);
        menus.add(tmp);
        tmp = navigation_others.add("Acerca de");
        tmp.setIcon(R.drawable.ic_baseline_info_24);
        menus.add(tmp);
        tmp = navigation_others.add("Cerrar Sesión");
        tmp.setIcon(R.drawable.icon_exit);
        tmp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
              //  Alerts.MessageToast(MainMenu.this, "cerrar sesión");
                user=new JSONObject();
                onBackPressed();
                return false;
            }
        });

        for (MenuItem temp : menus) {
            temp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    loadFragment(new GalleryFragment());
                    return true;
                }
            });
        }
    }


    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

}