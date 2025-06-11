package backend.backend_adprso.Controller;

import java.io.IOException;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.backend_adprso.Service.QRCodeService;

@RestController
public class PagoController {

    // FALTA *********************************************

    @Autowired
    private QRCodeService qrCodeService;

    @GetMapping("/api/pago/yape/qr")
    public ResponseEntity<byte[]> generarQRCode(@RequestParam String telefono, @RequestParam String monto) {
        try {
            String contenidoQR = String.format("https://yape.com.pe/pay?phone=%s&amount=%s", telefono, monto);
            byte[] qrImage = qrCodeService.generateQRCodeImage(contenidoQR, 350, 350);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return ResponseEntity.ok().headers(headers).body(qrImage);
        } catch (WriterException | IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
