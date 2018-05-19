package br.com.barrsoft.darkskyclient.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionRequestActivity {

    public static boolean PermissionRequest(int requestCode, Activity activity, String[] permissionsRequest){

//        Objetivos:
//        1º checar a versao SDK se é maior que 23
//        2º criar uma lista de permissao
//        3º varrer permissoes liberadas checando permissoes requeridas e adicionar as pendentes na lista
//        3ºA checar as permissoes requeridas pela activity usando ActivityCompat.checkSelfPermission==PackageManager.PERMISSION_GRANTED armazenar em boolean
//        3ºB adicionar permissoes pendentes (checar variavel boolean) na lista de permissao
//        4º apos a varredura checar se a lista vazia retorne true
//        5º lista preenchida solicitar permissao via ActivityCompat
//        6º no main no metodo onRequestPermissionsResult
//        6ºA varrer grantResults
//        6ºB verificar permissoes negadas
//        7º criar metodo para alertar ao usuario da necessidade de aceitar as permissoes

        if(Build.VERSION.SDK_INT >= 23){

            List<String> listPermission = new ArrayList<>();

            for (String permissionRequest: permissionsRequest) {

                boolean permissionCheck = ActivityCompat.checkSelfPermission(activity,permissionRequest) == PackageManager.PERMISSION_GRANTED;

                if(!permissionCheck){
                    listPermission.add(permissionRequest);
                }
            }

            if(listPermission.isEmpty()){
                return true;
            }

            String[]requestPermission = new String[listPermission.size()];
            listPermission.toArray(requestPermission);
            ActivityCompat.requestPermissions(activity,requestPermission,requestCode);
        }
        return true;
    }
}