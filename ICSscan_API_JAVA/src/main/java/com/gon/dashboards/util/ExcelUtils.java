package com.gon.dashboards.util;


import com.gon.dashboards.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelUtils {


    public static List<Account> readAccountExcel(InputStream inputStream, String fileName) {
        List<Account> list = new ArrayList<>();
        try {
            Workbook hssfWorkbook = null;
            if (fileName.endsWith("xlsx")) {

                hssfWorkbook = new XSSFWorkbook(inputStream);//Excel 2007

            } else if (fileName.endsWith("xls")) {
                hssfWorkbook = new HSSFWorkbook(inputStream);//Excel 2003

            }

            if (hssfWorkbook == null) {
                return null;
            }
            Sheet hssfSheet = hssfWorkbook.getSheetAt(0);
            if (hssfSheet == null) {
                return null;
            }

            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {

                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    Cell teamName = hssfRow.getCell(0);
                    Cell irisnetAddress = hssfRow.getCell(1);
                    Cell stargazeAddress = hssfRow.getCell(2);
                    Cell junoAddress = hssfRow.getCell(3);
                    Cell uptickAddress = hssfRow.getCell(4);
                    Cell omniFlixAddress = hssfRow.getCell(5);
                    Cell discordHandle = hssfRow.getCell(6);
                    Cell community = hssfRow.getCell(7);
                    Account account = new Account();
                    if (teamName != null) {
                        account.setTeamName(teamName.getStringCellValue());
                    }
                    if (irisnetAddress == null || stargazeAddress == null || junoAddress == null || uptickAddress == null || omniFlixAddress == null
                            || StringUtils.isEmpty(irisnetAddress.getStringCellValue())
                            || StringUtils.isEmpty(stargazeAddress.getStringCellValue())
                            || StringUtils.isEmpty(junoAddress.getStringCellValue())
                            || StringUtils.isEmpty(uptickAddress.getStringCellValue())
                            || StringUtils.isEmpty(omniFlixAddress.getStringCellValue())

                    ) {
                        break;
                    }

                    if (!irisnetAddress.getStringCellValue().startsWith("iaa")
                            || !stargazeAddress.getStringCellValue().startsWith("stars")
                            || !junoAddress.getStringCellValue().startsWith("juno")
                            || !uptickAddress.getStringCellValue().startsWith("uptick")
                            || !omniFlixAddress.getStringCellValue().startsWith("omniflix")) {
                        continue;
                    }


                    account.setIrisAddress(irisnetAddress.getStringCellValue());
                    account.setStargazeAddress(stargazeAddress.getStringCellValue());
                    account.setJunoAddress(junoAddress.getStringCellValue());
                    account.setUptickAddress(uptickAddress.getStringCellValue());
                    account.setOmniflixAddress(omniFlixAddress.getStringCellValue());
                    if (discordHandle != null) {
                        account.setDiscordHandle(discordHandle.getStringCellValue());
                    }
                    if (community != null) {
                        account.setCommunity(community.getStringCellValue());
                    }
                    list.add(account);
                }
            }

        } catch (Exception e) {
            log.error("error: {}", e.getMessage(), e);
        }
        return list;
    }


}
