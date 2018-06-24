package com.reggie.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.amap.api.fence.GeoFenceClient.GEOFENCE_IN;

public class MainActivity extends AppCompatActivity implements AMapLocationListener,GeoFenceListener{

    MapView mMapView;
    AMap aMap;
    MyLocationStyle myLocationStyle;

    List<LatLng> latLngs;
    List<Marker> markers;

    Location currentLocation;
    Location lastLocation;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption;

    GeoFenceClient mGeoFenceClient;
    //定义接收广播的action字符串
    public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMap(savedInstanceState);
        initData();
        initLocationClient();
        initGeoFenceClient();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    private void initMap(Bundle savedInstanceState){

        //获取地图控件引用
        mMapView = findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(5000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        UiSettings mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        mUiSettings.setScaleControlsEnabled(true);//控制比例尺控件是否显示

        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));

        currentLocation = aMap.getMyLocation();
    }

    private void initData(){
        latLngs = new ArrayList<>();
        markers = new ArrayList<>();
        //30.3118605582,120.3566297323
        LatLng latLng1 = new LatLng(30.3118605582,120.3566297323);
        Marker marker1 = aMap.addMarker(new MarkerOptions().position(latLng1).title("北京").snippet("DefaultMarker"));
        latLngs.add(latLng1);
        markers.add(marker1);
        //30.3129905291,120.3560396463
        LatLng latLng2 = new LatLng(30.3129905291,120.3560396463);
        Marker marker2 = aMap.addMarker(new MarkerOptions().position(latLng2).title("北京").snippet("DefaultMarker"));
        latLngs.add(latLng2);
        markers.add(marker2);
        //30.3117401507,120.3556319505
        LatLng latLng3 = new LatLng(30.3117401507,120.3556319505);
        Marker marker3 = aMap.addMarker(new MarkerOptions().position(latLng3).title("北京").snippet("DefaultMarker"));
        latLngs.add(latLng3);
        markers.add(marker3);
        //30.3139630346,120.3568335801
        LatLng latLng4 = new LatLng(30.3139630346,120.3568335801);
        Marker marker4 = aMap.addMarker(new MarkerOptions().position(latLng4).title("北京").snippet("DefaultMarker"));
        latLngs.add(latLng4);
        markers.add(marker4);
        //30.3151104097,120.3579164326
        LatLng latLng5 = new LatLng(30.3151104097,120.3579164326);
        Marker marker5 = aMap.addMarker(new MarkerOptions().position(latLng5).title("北京").snippet("DefaultMarker"));
        latLngs.add(latLng5);
        markers.add(marker5);
    }

    private void initLocationClient(){

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2500);

        if(null != mLocationClient){
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }

    }

    private void initGeoFenceClient(){

        mGeoFenceClient = new GeoFenceClient(getApplicationContext());
        mGeoFenceClient.setActivateAction(GEOFENCE_IN);

        for (LatLng ll : latLngs) {
            //Toast.makeText(this,latLngs.indexOf(ll),Toast.LENGTH_SHORT).show();
            mGeoFenceClient.addGeoFence (new DPoint(ll.latitude,ll.longitude),30F,"Site" + (latLngs.indexOf(ll) + 1));
        }


        //设置回调监听
        mGeoFenceClient.setGeoFenceListener(this);

        //创建并设置PendingIntent
        mGeoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);

        BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Objects.equals(intent.getAction(), GEOFENCE_BROADCAST_ACTION)) {
                    //解析广播内容
                    //获取Bundle
                    Bundle bundle = intent.getExtras();
                    //获取围栏行为：
                    assert bundle != null;
                    int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                    //获取自定义的围栏标识：
                    String customId = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                    //获取围栏ID:
                    String fenceId = bundle.getString(GeoFence.BUNDLE_KEY_FENCEID);
                    //获取当前有触发的围栏对象：
                    GeoFence fence = bundle.getParcelable(GeoFence.BUNDLE_KEY_FENCE);

                    for (Marker m : markers ) {
                        assert fence != null;
                        if (m.getPosition().latitude == fence.getCenter().getLatitude() &&  m.getPosition().longitude == fence.getCenter().getLongitude()){
                            Toast.makeText(getApplicationContext(), "reached to " + customId , Toast.LENGTH_SHORT).show();
                            m.remove();
                        }
                    }

                }
            }
        };

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(GEOFENCE_BROADCAST_ACTION);
        registerReceiver(mGeoFenceReceiver, filter);

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        lastLocation = currentLocation;
        currentLocation = aMapLocation;

        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        LatLng lastLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

        //Toast.makeText(this,"LocationChanged:\ncurrentLatLng:" + currentLatLng.toString() +"\nlastLatLng:" + lastLatLng.toString(),Toast.LENGTH_SHORT).show();

        PolylineOptions polylineOptions = new PolylineOptions().add(currentLatLng,lastLatLng).width(10)
                .color(Color.argb(255, 1, 1, 1));

        aMap.addPolyline(polylineOptions);
    }

    @Override
    public void onGeoFenceCreateFinished(List<GeoFence> list, int i, String s) {
        Toast.makeText(this,list.get(0).getCustomId() + " Created",Toast.LENGTH_SHORT).show();
    }

}
