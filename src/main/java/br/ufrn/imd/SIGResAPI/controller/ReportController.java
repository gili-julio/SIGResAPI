package br.ufrn.imd.SIGResAPI.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.SIGResAPI.dto.ReportRequestDTO;
import br.ufrn.imd.SIGResAPI.models.Order;
import br.ufrn.imd.SIGResAPI.models.Sale;
import lombok.RequiredArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    @Autowired
    OrderController orderController;

    @Autowired
    SaleController saleController;

    @PostMapping("/orders")
    public ResponseEntity<List<Order>> reportOrders(@RequestBody ReportRequestDTO body) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date initDate = sdf.parse(body.initDate());
        Date finalDate = sdf.parse(body.finalDate());
        List<Order> orders = orderController.getOrdersByDate(initDate, finalDate);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/extract")
    public ResponseEntity<List<Sale>> reportExtract(@RequestBody ReportRequestDTO body) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date initDate = sdf.parse(body.initDate());
        Date finalDate = sdf.parse(body.finalDate());
        List<Sale> sales = saleController.getSales(initDate, finalDate);
        return ResponseEntity.ok(sales);
    }

}
