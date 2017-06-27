package myapp

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class DolmetscherController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Dolmetscher.list(params), model:[dolmetscherCount: Dolmetscher.count()]
    }

    def show(Dolmetscher dolmetscher) {
        respond dolmetscher
    }

    def create() {
        respond new Dolmetscher(params)
    }

    @Transactional
    def save(Dolmetscher dolmetscher) {
        if (dolmetscher == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (dolmetscher.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond dolmetscher.errors, view:'create'
            return
        }

        dolmetscher.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'dolmetscher.label', default: 'Dolmetscher'), dolmetscher.id])
                redirect dolmetscher
            }
            '*' { respond dolmetscher, [status: CREATED] }
        }
    }

    def edit(Dolmetscher dolmetscher) {
        respond dolmetscher
    }

    @Transactional
    def update(Dolmetscher dolmetscher) {
        if (dolmetscher == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (dolmetscher.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond dolmetscher.errors, view:'edit'
            return
        }

        dolmetscher.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'dolmetscher.label', default: 'Dolmetscher'), dolmetscher.id])
                redirect dolmetscher
            }
            '*'{ respond dolmetscher, [status: OK] }
        }
    }

    @Transactional
    def delete(Dolmetscher dolmetscher) {

        if (dolmetscher == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        dolmetscher.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'dolmetscher.label', default: 'Dolmetscher'), dolmetscher.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'dolmetscher.label', default: 'Dolmetscher'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
