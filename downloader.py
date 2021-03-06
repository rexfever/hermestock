# -*- coding: utf-8 -*-
from time import sleep
from selenium import webdriver
from selenium.webdriver.support.select import Select
from datetime import datetime
import os

'''
1. 목적페이지 이동 
2. 날짜 및 검색항목 설정 
3. 시장 선택 
4. 투자자 선택 후 다운로드(function화)
5. 시장 수만큼 3,4번째 항 반복
'''

webdriver_options = webdriver.ChromeOptions()
webdriver_options.add_argument('headless')

DRIVER = webdriver.Chrome('./chromedriver', options=webdriver_options)
#DRIVER = webdriver.Chrome('./chromedriver')

# 시장, 매수주체 선언
MARKETS = [['4', 'kospi'], ['6', 'kosdaq']]
BUYERS = [['7050', 'KIK'], ['9000', 'FO']]


# 날짜 및 검색 항목 설정
def set_date(date):
    DRIVER.get('http://data.krx.co.kr/contents/MDC/MDI/mdiLoader/index.cmd?menuId=MDC0201020303')
    sleep(3)
    target_sdate = DRIVER.find_element_by_name('strtDd')
    target_edate = DRIVER.find_element_by_name('endDd')
    target_sdate.clear()
    target_edate.clear()
    target_sdate.send_keys(datetime.now().strftime(date))
    target_edate.send_keys(datetime.now().strftime(date))


# 매수 주체 선택 후 다운로드
def _select_buyer(element):
    for buyer in BUYERS:
        element.select_by_value(buyer[0])
        DRIVER.find_element_by_class_name('btn-board.btn-board-search').click()
        sleep(3)
        excel_button = DRIVER.find_element_by_xpath("//*[contains(text(), 'CSV')]")
        excel_button.click()


# 시장 선택
def _select_market():
    for market in MARKETS:
        DRIVER.find_element_by_css_selector(f'.design-fieldset > form > dl > dd > input:nth-child({market[0]})').click()
        select_element_id = DRIVER.find_element_by_name('var_invr_cd').get_attribute("id")
        selected_element = Select(DRIVER.find_element_by_id(select_element_id))
        _select_buyer(selected_element)


# 매수 주체 선택 후 다운로드
def _select_buyer(element, buyer):
    element.select_by_value(buyer)
    DRIVER.find_element_by_class_name('btn_black.btn_component_search').click()
    sleep(3)
    excel_button = DRIVER.find_element_by_xpath('//*[@id="MDCSTAT024_FORM"]/div[2]/div/p[2]/button[2]')
    excel_button.click()
    sleep(1)
    csv_button = DRIVER.find_element_by_link_text('CSV')
    csv_button.click()


# 시장 선택
def select_market(data_source):
    DRIVER.find_element_by_css_selector(f'#MDCSTAT024_FORM > div.search_tb > div > table > tbody > tr:nth-child(1) > td > label:nth-child({data_source[0]})').click()
    select_element_id = DRIVER.find_element_by_name('invstTpCd').get_attribute("id")
    selected_element = Select(DRIVER.find_element_by_id(select_element_id))
    _select_buyer(selected_element, data_source[1])


def close_window():
    DRIVER.close()


