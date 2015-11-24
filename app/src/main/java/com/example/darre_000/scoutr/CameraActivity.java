//package com.example.darre_000.scoutr;
//
//import android.net.Uri;
//import android.support.v7.app.AppCompatActivity;
//import android.view.Menu;
//import android.view.MenuItem;
//
//public class CameraActivity extends AppCompatActivity {
//
//    private  static  String logTrack = "CameraAppLog";
//    private static int PICTURE_TAKE = 1;
//    private Uri imageUri;
//
////    @Override
////    protected void onActivityResult(int requestCdoe, int resultCode, Intent intent){
////        super.onActivityResult(resultCode,resultCode,intent);
////
////        if (resultCode == Activity.RESULT_OK){
////            Uri chosenImage = imageUri;
////            getContentResolver().notifyChange(chosenImage,null);
////
////            ImageView imageView = (ImageView)findViewById(R.id.image_camera);
////            ContentResolver cr = getContentResolver();
////            Bitmap bitmap;
////
////            try {
////                bitmap = MediaStore.Images.Media.getBitmap(cr, imageUri);
////                imageView.setImageBitmap(bitmap);
////            } catch(Exception e) {
////                Log.e(logTrack, e.toString());
////            }
////        }
////    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.camera_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}