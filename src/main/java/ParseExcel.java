import java.io.InputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Exception;
import java.io.FileNotFoundException;
import java.awt.*;
import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.Row;  
import org.apache.poi.ss.usermodel.Sheet;  
import org.apache.poi.ss.usermodel.Workbook;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
public class ParseExcel {
    static String path;
    static String fileName;
    public static void main( String[] args ) {
        selectFileGui();
        readExcel(path, fileName);
        System.exit(0); 
    }
    public ParseExcel(  ) {
        
    }
    public static void selectFileGui(){
        Frame frame = new Frame("select Excel");
        FileDialog openFileDialog = new FileDialog(frame ,"Open File",FileDialog.LOAD); 
        openFileDialog.setVisible(true);
        path = openFileDialog.getDirectory();
        fileName = openFileDialog.getFile();
    }
    public static void readExcel(String path, String fileName) {
        FileOutputStream out = null;
        try
        {
            File file = new File(path+fileName);
            FileInputStream in = new FileInputStream(file);
            Workbook wb = new XSSFWorkbook(in);
            Sheet sheet1 = wb.getSheetAt(0);  
            for (Row row : sheet1) {  
                    if(row.getRowNum() > 0)
                        ParseRow(row); 
            }  
            out = new FileOutputStream(file);
            wb.write(out);
            
            
        }
        catch (FileNotFoundException e)
        {
            System.out.println("file not found"); 
        }
        catch (IOException e){
            System.out.println("io error");
        }
        finally{
            if(out != null){
                try{
                    out.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

    }
    public static void ParseRow(Row row){
        System.out.println("parse the "+row.getRowNum());
        try{
            
            Cell province = row.getCell(0), 
                 prefectrue = row.getCell(1),
                 name = row.getCell(2),
                 atm = row.getCell(3),
                 bank = row.getCell(4),
                 bank_a = row.getCell(5),
                 town = row.getCell(6),
                 townBank = row.getCell(7),
                 rate = row.getCell(9);
            if(atm == null)
                atm = row.createCell(3);
            if(bank == null)
                bank = row.createCell(4);
            if(bank_a == null)
                bank_a = row.createCell(5);
            if(townBank == null)
                townBank = row.createCell(7);
            if(rate == null)
                rate = row.createCell(9);
            String region, poiAtm, poiBank, poiTownBank;
            if(prefectrue == null ||prefectrue.getCellType() == HSSFCell.CELL_TYPE_BLANK||
                    prefectrue.getStringCellValue().equals("无")){
                if(province == null ||province.getCellType() == HSSFCell.CELL_TYPE_BLANK||
                        province.getStringCellValue().equals("无"))
                    return;
                else
                    region = province.getStringCellValue();
            }
            else
                region = prefectrue.getStringCellValue();

            poiAtm = name.getStringCellValue()+"$"+"ATM";
            poiBank = name.getStringCellValue()+"$"+"银行";

            int atmCount = Query.getPoiCount(region, poiAtm);
            int bankCount = Query.getPoiCount(region, poiBank);

            atm.setCellValue(atmCount);
            bank.setCellValue(bankCount);
            bank_a.setCellValue(bankCount-atmCount);
            if(bankCount != 0)
                rate.setCellValue(atmCount / (double)bankCount);
            else
                rate.setCellValue(0);
            if(town != null && town.getCellType() != HSSFCell.CELL_TYPE_BLANK){
                
                poiTownBank = town.getStringCellValue()+"$"+"银行";
                int townBankCount = Query.getPoiCount(region, poiTownBank);
                townBank.setCellValue(townBankCount);
            }

        }catch(Exception e){
            System.out.println("this "+ row.getRowNum()+" row has Exception "+e.getMessage());
            e.printStackTrace();
        }
                    

    }
}
