package net.borak.connector.bora.nlp.parser

import org.junit.Test

class PartnersParserTest {

    private val text: String = "<div><p><span xml:lang=\"en-US\" ><p><span>GRUPO MC ARRI S.A.</span></p> </span></p><p></p><p><span xml:lang=\"en-US\" ></span></p><p>1) 19/12/18; 2) Marciano Arriola Martínez, 6/3/58, DNI92973789, Ibarro la 7257 Departamento 1, Caba; Elena Beatriz Figueredo Giubi, 21/8/75, DNI94833613, 9 de Setiembre 4405, Lanus, Pcia.Bs.As; ambos paragua yos, solteros, comerciantes; 3) GRUPO MC ARRI S.A.; 4) 99 años; 5) construcción de inmuebles urbanos y/o rurales, obras civiles, obras viales, desagües, loteos, servicios de reparación, refacción y mantenimiento de inmuebles, administración y explotación de inmuebles propios, de terceros y asociada a terceros; 6) $ 100.000,- acciones $ 1,- cada una; 7) 30/11; 8) Marciano Arriola Martínez 90%; Elena Beatriz Figueredo Giubi 10%; 9) Avda. Corrientes 1670, Piso 12º Departamento B, Caba; 10) Presidente: Marciano Arriola Martínez; Directora Suplen te: Elena Beatriz Figueredo Giubi; ambos domicilio especial: Avda. Corrientes 1670, Piso 12º Departamento B, Caba; Autorizado según instrumento público Esc. Nº 677 de fecha 19/12/2018 Reg. Nº 1919 Rafael SALAVE - T°: 114 F°: 344 C.P.A.C.F.</p><p>e. 02/01/2019 N° 99821/18 v. 02/01/2019</p></div>"

    @Test fun parse() {
        val parser = PartnersParser()
        val partners = parser.parse(text)

        println(partners)
    }
}